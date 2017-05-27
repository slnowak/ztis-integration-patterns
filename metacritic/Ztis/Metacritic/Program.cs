using Metacritic.Dto;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

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
            foreach (var item in GetRecomendations(name))
            {
                Console.WriteLine($"{item.Name} -- {item.ApiDetailUrl}");
            }

            Console.ReadLine();


            var rabbitConfig = new RabbitConfigFactory().CreateDefault();

            var connectionFactory = new ConnectionFactory()
            {
                Port = rabbitConfig.Port,
                HostName = rabbitConfig.Hostname,
                UserName = rabbitConfig.UserName,
                Password = rabbitConfig.Password
            };

            using (var connection = connectionFactory.CreateConnection())
            {
                using (var channel = connection.CreateModel())
                {
                    channel.QueueDeclare(queue: "sample_queue", autoDelete: false);


                    var consumer = new EventingBasicConsumer(channel);
                    consumer.Received += (model, arg) =>
                    {
                        var body = arg.Body;
                        var message = Encoding.UTF8.GetString(body);

                        Console.WriteLine(message);
                    };

                    channel.BasicConsume(queue: "sample_queue", noAck: true, consumer: consumer);
                }
            }
        }
    }
}