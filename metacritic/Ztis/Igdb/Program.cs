using Newtonsoft.Json;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Igdb
{
    class Game
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Url { get; set; }
        public string Summary { get; set; }
        public double Rating { get; set; }
        public int[] Games { get; set; }
        public int[] Genres { get; set; }
    }

    class SimilarGame
    {
        public int Id { get; set; }
        public string Name { get; set; }
    }

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

        public Task<IList<Game>> SearchGame(string name)
        {
            var resource = $"/games/?fields=*&limit=10&offset=0&search={name}";
            var request = NewRequest(resource);

            var taskSource = new TaskCompletionSource<IList<Game>>();

            var handle = _client.ExecuteAsync(request, resp =>
            {
                var content = resp.Content;
                var games = JsonConvert.DeserializeObject<Game[]>(content);

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

        public async Task<IList<Game>> GetRecomendations(string gameName)
        {
            var games = await SearchGame(gameName);

            var bestMatch = games.First();

            var game = await GetGameById(bestMatch.Id);

            var recomendations = Task.WhenAll(game.Games.Select(gid => GetGameById(gid)).ToArray());

            var recArray = await recomendations;

            return recArray.ToList();
        }
    }


    class Program
    {
        static void Main(string[] args)
        {
            var config = new IgdbConfigFactory().Create();
            var api = new IgdbApi(config);

            var name = "gothic";
            var recomendations = api.GetRecomendations(name).Result;

            Console.WriteLine($"{name} recomendations");
            foreach (var game in recomendations)
            {
                Console.WriteLine($"{game.Name}");
            }

            Console.ReadLine();
        }
    }
}