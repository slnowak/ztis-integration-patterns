namespace Metacritic.Dto
{
    class SearchGameResponse
    {
        public int StatusCode { get; set; }

        public string Error { get; set; }

        public SearchGameResult[] Results { get; set; }
    }
}