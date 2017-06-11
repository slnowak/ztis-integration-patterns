namespace Metacritic.Dto
{
    class GameMessage
    {
        public int GameId { get; set; }
        public string Title { get; set; }
        public string Description { get; set; }
        public string[] Genres { get; set; }
    }
}