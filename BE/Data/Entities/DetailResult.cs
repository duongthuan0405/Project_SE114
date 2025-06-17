using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    public class DetailResult
    {
        public string AttemptQuizId { get; set; }

        [Required]
        public string AnswerId { get; set; }

        [ForeignKey("AttemptQuizId")]
        public AttemptQuiz? OAttemptQuiz { get; set; }

        [ForeignKey("AnswerId")]
        [Required]
        public Answer OAnswer { get; set; }

    }
}