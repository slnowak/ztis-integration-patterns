using Metacritic.Dto;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
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
        static void Main(string[] args)
        {
            var config = new MetacriticConfigFactory().Create();
            var metacritic = new MetacriticApi(config);

            var rabbitConfig = new RabbitConfigFactory().Create();
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
                    // input queue
                    // TODO move to configuration
                    var exchangeName = "steam";
                    var topic = "game_bought";

                    channel.ExchangeDeclare(exchange: exchangeName, type: "topic");
                    var inputQueueName = channel.QueueDeclare().QueueName;

                    channel.QueueBind(queue: inputQueueName, exchange: exchangeName, routingKey: topic);

                    // output exchange
                    var exchangeName2 = "metacritic";
                    var topic2 = "recomendations";

                    channel.ExchangeDeclare(exchange: exchangeName2, type: "topic");
                    


                    var consumer = new QueueingBasicConsumer(channel);

                    channel.BasicConsume(queue: inputQueueName, noAck: true, consumer: consumer);

                    Console.WriteLine("metacritic up");

                    while (true)
                    {
                        var arg = consumer.Queue.Dequeue();

                        var body = arg.Body;
                        var message = Encoding.UTF8.GetString(body);

                        var gameMessage = JsonConvert.DeserializeObject<GameMessage>(message);
                        
                        var recomendations = metacritic.GetRecomendations(gameMessage.Title).Result;

                        var bytes = SerializeResults(recomendations, gameMessage);

                        channel.BasicPublish(exchange: exchangeName2, routingKey: topic2, body: bytes);
                    }
                }
            }
        }

        private static byte[] SerializeResults(IList<Recommendation> recomendations,GameMessage gameMessage)
        {
            var resultJson = new JObject
            {
                ["gameId"] = gameMessage.GameId,
                ["title"] = gameMessage.Title,
                ["recommendations"] = new JArray(recomendations.Select(rec =>
                    new JObject
                    {
                        ["id"] = rec.Id,
                        ["name"] = rec.Name,
                        ["detailsApiUrl"] = rec.ApiDetailUrl,
                        ["detailsSiteUrl"] = rec.SiteDetailUrl
                    }))
            };
            var resultString = resultJson.ToString();
            var resultBytes = Encoding.UTF8.GetBytes(resultString);

            return resultBytes;
        }
    }
}