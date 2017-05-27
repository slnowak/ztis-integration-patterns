using Newtonsoft.Json.Linq;
using RestSharp;

namespace Metacritic
{
    class MetacriticApi
    {
        private static readonly string baseUrl = "http://www.giantbomb.com/api/";

        private RestClient _client;
        private MetacriticConfig _config;

        public MetacriticApi(MetacriticConfig config)
        {
            _config = config;
            _client = new RestClient(baseUrl);
        }

        public JObject Search(string name)
        {
            var request = new RestRequest("search", Method.GET);
            request.AddHeader("User-Agent", @"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");

            request.AddQueryParameter("api_key", _config.ApiKey);
            request.AddQueryParameter("format", "json");
            request.AddQueryParameter("query", name);
            request.AddQueryParameter("resources", "game");

            var result = _client.ExecuteAsync(request).Result;
            var content = result.Content;

            return JObject.Parse(content);
        }
    }
}