using Metacritic.Dto;
using Newtonsoft.Json;
using RestSharp;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

namespace Metacritic
{
    class MetacriticApi
    {
        private static readonly string baseUrl = "http://www.giantbomb.com/api/";
        private readonly string userAgent = @"Mozilla/5.0 (Macintosh; InKtel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

        private RestClient _client;
        private MetacriticConfig _config;

        public MetacriticApi(MetacriticConfig config)
        {
            _config = config;
            _client = new RestClient(baseUrl);
        }

        private RestRequest NewGetRequest(string resource)
        {
            var request = new RestRequest(resource, Method.GET);

            request.AddHeader("User-Agent", userAgent);
            request.AddQueryParameter("api_key", _config.ApiKey);
            request.AddQueryParameter("format", "json");

            return request;
        }

        private Task<TData> ExecuteAsync<TResponse, TData>(RestRequest request, Func<TResponse, TData> callback)
            where TResponse : new()
        {
            var taskSource = new TaskCompletionSource<TData>();

            _client.ExecuteAsync<TResponse>(request, response =>
            {
                if (response.StatusCode == HttpStatusCode.OK)
                {
                    var data = callback(JsonConvert.DeserializeObject<TResponse>(response.Content));
                    taskSource.SetResult(data);
                }
                else
                {
                    taskSource.SetException(response.ErrorException ?? new Exception($"status code {response.StatusCode}"));
                }
            });

            return taskSource.Task;
        }

        public Task<IList<SearchGameResult>> SearchGame(string name)
        {
            var request = NewGetRequest("search");
            request.AddQueryParameter("query", name);
            request.AddQueryParameter("resources", "game");
            request.AddQueryParameter("field_list", "id,name,deck,number_of_user_reviews,api_detail_url,original_game_rating");
            request.AddQueryParameter("limit", "1");

            var task = ExecuteAsync<SearchGameResponse, IList<SearchGameResult>>(request, response => response.Results);

            return task;
        }

        public Task<Game> GetGameById(int id)
        {
            var request = NewGetRequest("game/3030-{id}");
            request.AddUrlSegment("id", id);
            request.AddQueryParameter("field_list", "id,name,similar_games");

            var task = ExecuteAsync<GameByIdResponse, Game>(request, response => response.Results);

            return task;
        }


        public async Task<IList<Recommendation>> GetRecomendations(string gameName)
        {
            var games = await SearchGame(gameName);

            var bestMatch = games.FirstOrDefault();

            if (bestMatch != null)
            {
                var game = await GetGameById(bestMatch.Id);

                return (game.SimilarGames ?? new Recommendation[0]).ToList();
            }
            else
            {
                return new List<Recommendation>();
            }
        }
    }
}