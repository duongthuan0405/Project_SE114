using System.ComponentModel.DataAnnotations;

namespace BE.DTOs
{
    public class QuizCreateRequestDTO
    {
        [Required]
        public string Name { get; set; } = "";
        public string? Description { get; set; } = "";
        [Required]
        public DateTime StartTime { get; set; }
        [Required]
        public DateTime DueTime { get; set; }
        [Required]
        public string CourseId { get; set; } = "";
        [Required]
        public bool IsPublished { get; set; } = false;
      
    }
}