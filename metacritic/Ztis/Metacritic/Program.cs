using System;

namespace Metacritic
{
    class Program
    {
        static void Main(string[] args)
        {
            var config = new MetacriticConfigFactory().Create();
            var metacritic = new MetacriticApi(config);

            var result = metacritic.Search("gothic ii");

            var results = result["results"];
            foreach(var item in results)
            {
                var name = item["name"];
                Console.WriteLine($"{name}\n");
            }

            Console.ReadLine();
        }
    }
}