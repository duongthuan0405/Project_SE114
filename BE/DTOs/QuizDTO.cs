
namespace BE.DTOs
{
    public class QuizDTO
    {
        public string Id { get; internal set; }
        public string Name { get; internal set; }
        public string? Description { get; internal set; }
        public DateTime StartTime { get; internal set; }
        public DateTime DueTime { get; internal set; }
        public int StateOfAttemption { get; internal set; }
    }
}