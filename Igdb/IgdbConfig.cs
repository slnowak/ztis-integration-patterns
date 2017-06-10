using System;

namespace Igdb
{
    class IgdbConfig
    {
        public string ApiKey { get; set; }
    }

    class IgdbConfigFactory
    {
        public IgdbConfig Create()
        {
            var apiKey = Environment.GetEnvironmentVariable("IGDB_KEY")
                ?? throw new Exception("no igdb api_key found");

            return new IgdbConfig
            {
                ApiKey = apiKey
            };
        }
    }
}