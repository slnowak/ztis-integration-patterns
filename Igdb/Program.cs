using System;
using System.Linq;

namespace Igdb
{
    class Program
    {
        static void Main(string[] args)
        {
            var config = new IgdbConfigFactory().Create();
            var api = new IgdbApi(config);

            var name = "gothic";

            var games = api.SearchGame(name).Result;
            var bestMatch = games.First();

            var recomendations = api.GetRecomendations(bestMatch.Id).Result;

            Console.WriteLine($"{name} recomendations");
            foreach (var game in recomendations)
            {
                Console.WriteLine($"{game.Name}");
            }

            Console.ReadLine();
        }
    }
}