using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class QuizWithScoreDTO
    {
        public QuizDTO Quiz { get; set; }
        public int TotalCorrectAnswer { get; set; } = 0;
        public int TotalQuestions { get; set; } = 1;
        public bool IsSubmitted { get; set; } = false;
    }
}