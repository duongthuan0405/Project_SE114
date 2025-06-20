using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("QuestionDTO")]
    public class Question
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "NVarChar(1000)")]
        [MaxLength(1000)]
        public string Content { get; set; }

        public string? QuizId { get; set; }

        [ForeignKey("QuizId")]
        public Quiz? OQuiz { get; set; }

        public List<Answer> LAnswers { get; set; } = new List<Answer>();
    }
}