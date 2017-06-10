using Newtonsoft.Json;
using RestSharp;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Igdb.Dto;

namespace Igdb
{
    class IgdbApi
    {
        private static readonly string baseUrl = "https://igdbcom-internet-game-database-v1.p.mashape.com/";

        private readonly RestClient _client;
        private readonly IgdbConfig _config;

        public IgdbApi(IgdbConfig config)
        {
            _config = config;
            _client = new RestClient(baseUrl);
        }

        private RestRequest NewRequest(string resource)
        {
            var request = new RestRequest(resource, Method.GET);

            request.AddHeader("X-Mashape-Key", _config.ApiKey);
            request.AddHeader("Accept", "application/json");

            return request;
        }

        public Task<IList<SearchGameResult>> SearchGame(string name)
        {
            var resource = $"/games/?fields=id,name&limit=10&offset=0&search={name}";
            var request = NewRequest(resource);

            var taskSource = new TaskCompletionSource<IList<SearchGameResult>>();

            var handle = _client.ExecuteAsync(request, resp =>
            {
                var content = resp.Content;
                var games = JsonConvert.DeserializeObject<SearchGameResult[]>(content);

                taskSource.SetResult(games.ToList());
            });

            return taskSource.Task;
        }

        public Task<Game> GetGameById(int id)
        {
            var resource = $"/games/{id}?fields=*";
            var gameRequest = NewRequest(resource);

            var taskSource = new TaskCompletionSource<Game>();

            var handle = _client.ExecuteAsync(gameRequest, response =>
            {
                var game = JsonConvert.DeserializeObject<Game[]>(response.Content).Single();

                taskSource.SetResult(game);
            });

            return taskSource.Task;
        }

        public async Task<IList<Game>> GetRecomendations(int id)
        {
            var game = await GetGameById(id);

            var recomendations = Task.WhenAll(game.Games.Select(gid => GetGameById(gid)).ToArray());

            var recArray = await recomendations;

            return recArray.ToList();
        }
    }
}