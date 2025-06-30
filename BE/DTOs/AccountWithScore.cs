using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class AccountWithScore
    {
        public AccountInfoDTO Account { get; set; }
        public int TotalCorrectAnswer { get; set; } = 0;
        public int TotalQuestions { get; set; } = 1;
        public bool IsSubmitted { get; set; } = false;
        public DateTime? FinishedAt { get; set; } = null;
        public DateTime? StartedAt { get; set; } = null;
    }
}