using Newtonsoft.Json;

namespace Metacritic.Dto
{
    class Game
    {
        public int Id { get; set; }

        public string Name { get; set; }

        [JsonProperty("similar_games")]
        public Recommendation[] SimilarGames { get; set; }
    }
}