namespace Metacritic.Dto
{
    class GameByIdResponse
    {
        public int StatusCode { get; set; }

        public string Error { get; set; }

        public Game Results { get; set; }
    }
}