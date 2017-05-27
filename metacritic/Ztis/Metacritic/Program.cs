using Metacritic.Dto;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Metacritic
{
    class Program
    {
        private static IList<SimilarGame> GetRecomendations(string gameName)
        {
            var config = new MetacriticConfigFactory().Create();
            var metacritic = new MetacriticApi(config);

            var games = metacritic.SearchGame(gameName);

            var bestMatch = games.First();

            var game = metacritic.GetGameById(bestMatch.Id);

            return game.SimilarGames.ToList();
        }

        static void Main(string[] args)
        {
            var name = "gothic ii";


            Console.WriteLine($"games similar to {name}");
            foreach(var item in GetRecomendations(name))
            {
                Console.WriteLine($"{item.Name} -- {item.ApiDetailUrl}");
            }

            Console.ReadLine();
        }
    }
}