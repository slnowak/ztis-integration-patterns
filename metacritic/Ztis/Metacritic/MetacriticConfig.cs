using System;

namespace Metacritic
{
    class MetacriticConfig
    {
        public string ApiKey { get; set; }
    }

    class MetacriticConfigFactory
    {
        public MetacriticConfig Create()
        {
            var apikey = Environment.GetEnvironmentVariable("META_KEY") 
                ?? throw new Exception("no metacritic api_key found");

            return new MetacriticConfig
            {
                ApiKey = apikey
            };
        }
    }
}