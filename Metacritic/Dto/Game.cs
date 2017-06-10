using Newtonsoft.Json;

namespace Metacritic.Dto
{
    class Game
    {
        public int Id { get; set; }

        public string Name { get; set; }

        [JsonProperty("similar_games")]
        public Recomendation[] SimilarGames { get; set; }
    }
}