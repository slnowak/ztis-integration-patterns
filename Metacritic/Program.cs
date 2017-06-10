using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;

namespace Metacritic
{
    class GameMessage
    {
        public int GameId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public string[] Genres { get; set; }
    }

    class Program
    {
        static void Main(string[] args)
        {
            var config = new MetacriticConfigFactory().Create();
            var metacritic = new MetacriticApi(config);


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
                    


                    var consumer = new EventingBasicConsumer(channel);
                    consumer.Received += (model, arg) =>
                    {
                        var body = arg.Body;
                        var message = Encoding.UTF8.GetString(body);

                        var gameMessage = JsonConvert.DeserializeObject<GameMessage>(message);
                        
                        var recomendations = metacritic.GetRecomendations(gameMessage.Title).Result;

                        var result = new JObject
                        {
                            ["gameId"] = gameMessage.GameId,
                            ["title"] = gameMessage.Title,
                            ["recomendations"] = JsonConvert.SerializeObject(recomendations)
                        };
                        var bytes = Encoding.UTF8.GetBytes(result.ToString());

                        channel.BasicPublish(exchange: exchangeName2, routingKey: topic2, body: bytes);
                    };

                    channel.BasicConsume(queue: inputQueueName, noAck: true, consumer: consumer);

                    Console.ReadLine();
                }
            }
        }
    }
}