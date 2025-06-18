using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("Quiz")]
    public class Quiz
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "NVarChar(30)")]
        [MaxLength(30)]
        public string Name { get; set; }

        [Column(TypeName = "NVarChar(100)")]
        [MaxLength(100)]
        public string? Description { get; set; }

        [Required]
        public DateTime StartTime { get; set; }

        [Required]
        public DateTime DueTime { get; set; }

        [Required]
        public string CourseID { get; set; }

        [ForeignKey("CourseID")]
        [Required]
        public Course OCourse { get; set; }

        [Required]
        public bool IsPublished { get; set; } = false;

        public List<Question> Questions { get; set; } = new List<Question>();

        public List<AttemptQuiz> LAttemptQuizzes { get; set; } = new List<AttemptQuiz>();
    }
}