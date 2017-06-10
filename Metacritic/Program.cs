using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;

namespace Metacritic
{
    class Program
    {
        static void Main(string[] args)
        {
            var config = new MetacriticConfigFactory().Create();
            var metacritic = new MetacriticApi(config);


            var name = "gothic ii";

            Console.WriteLine($"games similar to {name}");
            var recomendations = metacritic.GetRecomendations(name).Result;

            foreach (var item in recomendations)
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