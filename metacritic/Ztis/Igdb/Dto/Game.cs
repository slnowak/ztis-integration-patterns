namespace Igdb.Dto
{
    class Game
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Url { get; set; }
        public string Summary { get; set; }
        public double Rating { get; set; }
        public int[] Games { get; set; }
        public int[] Genres { get; set; }
    }
}