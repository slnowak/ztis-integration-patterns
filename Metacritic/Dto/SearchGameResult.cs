namespace Metacritic.Dto
{
    class SearchGameResult
    {
        public int Id { get; set; }

        public string Name { get; set; }

        public string Deck { get; set; }

        public int NumberOfUserReviews { get; set; }

        public string ApiDetailUrl { get; set; }

        public string OriginalGameRating { get; set; }
    }
}