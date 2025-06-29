using BE.Data.Entities;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BE.DTOs
{
    public class AttemptQuizDTO
    {
        public string Id { get; set; }

        public string QuizId { get; set; }
        public string QuizName { get; set; }
        public string CourseName { get; set; }
        public string AccountId { get; set; }
        public DateTime AttemptTime { get; set; }
        public DateTime FinishTime { get; set; }
        public bool IsSubmitted { get; set; } = false;
    }
}