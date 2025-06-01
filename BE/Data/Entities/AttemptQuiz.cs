using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("AttemptQuiz")]
    public class AttemptQuiz
    {
        [Key]
        [Column(TypeName = "Char(6)")]
        [MinLength(6), MaxLength(6)]
        public string Id { get; set; }

        [Required]
        public string QuizId { get; set; }

        [Required]
        public string AccountId { get; set; }

        [Required]
        public DateTime AttemptTime { get; set; }

        [Required]
        public DateTime FinishTime { get; set; }

        [ForeignKey("QuizId")]
        [Required]
        public Quiz OQuiz { get; set; }

        [ForeignKey("AccountId")]
        [Required]
        public Account OAccount { get; set; }
        public List<DetailResult> LDetailResults { get; set; } = new List<DetailResult>();
    }
}