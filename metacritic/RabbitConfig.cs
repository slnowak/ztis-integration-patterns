using System;

namespace Metacritic
{
    class RabbitConfig
    {
        public string Hostname { get; set; }

        public int Port { get; set; }

        public string UserName { get; set; }

        public string Password { get; set; }
    }

    class RabbitConfigFactory
    {
        public RabbitConfig Create()
        {
            var port = Environment.GetEnvironmentVariable("RABBIT_PORT")
                ?? throw new Exception("rabbit port was not defined");

            var portNr = int.Parse(port);

            var hostname = Environment.GetEnvironmentVariable("RABBIT_HOST")
                ?? throw new Exception("rabbit hostname was not defined");

            var user = Environment.GetEnvironmentVariable("RABBIT_USER")
                ?? throw new Exception("rabbit username was not defined");

            var password = Environment.GetEnvironmentVariable("RABBIT_PASSWORD")
                ?? throw new Exception("rabbit password was not defined");

            return new RabbitConfig
            {
                Hostname = hostname,
                Port = portNr,
                Password = password,
                UserName = user,
            };
        }

        public RabbitConfig CreateDefault()
        {
            return new RabbitConfig
            {
                Port = 5672,
                Hostname = "localhost",
                UserName = "guest",
                Password = "guest"
            };
        }
    }
}