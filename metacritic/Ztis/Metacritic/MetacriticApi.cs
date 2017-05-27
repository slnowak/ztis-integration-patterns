using Metacritic.Dto;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RestSharp;
using System.Collections.Generic;
using System.Linq;

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

        private JObject ExecuteRequest(RestRequest request)
        {
            var result = _client.ExecuteAsync(request).Result;
            var content = result.Content;

            return JObject.Parse(content);
        }

        public IList<SearchGameResult> SearchGame(string name)
        {
            var request = new RestRequest("search", Method.GET);
            request.AddHeader("User-Agent", userAgent);

            request.AddQueryParameter("api_key", _config.ApiKey);
            request.AddQueryParameter("format", "json");
            request.AddQueryParameter("query", name);
            request.AddQueryParameter("resources", "game");

            var json = ExecuteRequest(request);

            var games = json["results"]
                .Select(item => JsonConvert.DeserializeObject<SearchGameResult>(item.ToString()))
                .ToList();

            return games;
        }

        public Game GetGameById(int id)
        {
            var request = new RestRequest("game/3030-{id}");
            request.AddHeader("User-Agent", userAgent);

            request.AddUrlSegment("id", id);
            request.AddQueryParameter("api_key", _config.ApiKey);
            request.AddQueryParameter("format", "json");

            var json= ExecuteRequest(request);

            var game = JsonConvert.DeserializeObject<Game>(json["results"].ToString());

            return game;
        }
    }
}