
namespace BE.DTOs
{
    public class QuizDTO
    {
        public string Id { get; set; } = "";
        public string Name { get; set; } = "";
        public string? Description { get; set; } = "";
        public DateTime StartTime { get; set; } 
        public DateTime DueTime { get; set; }
        public string CourseId { get; set; } = "";
        public string CourseName { get; set; } = "";
        public bool IsPublished { get; set; } = false;
        public string StatusOfAttempt { get; set; } = ""; 
    }
}