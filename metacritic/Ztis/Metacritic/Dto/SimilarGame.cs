using Newtonsoft.Json;

namespace Metacritic.Dto
{
    class SimilarGame
    {
        public int Id { get; set; }

        public string Name { get; set; }

        [JsonProperty("api_detail_url")]
        public string ApiDetailUrl { get; set; }

        [JsonProperty("site_detail_url")]
        public string SiteDetailUrl { get; set; }
    }
}