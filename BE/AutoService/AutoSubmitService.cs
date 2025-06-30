using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BE.Data.Database;
using Microsoft.EntityFrameworkCore;

namespace BE.AutoService
{
    public class AutoSubmitService : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;
        private readonly TimeSpan _interval = TimeSpan.FromMinutes(0.5);
        public AutoSubmitService(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                await SubmitExpiredQuizzesAsync();
                await Task.Delay(_interval, stoppingToken);
            }
        }

        private async Task SubmitExpiredQuizzesAsync()
        {
            using (var scope = _serviceProvider.CreateScope())
            {
                var db = scope.ServiceProvider.GetRequiredService<MyAppDBContext>();
                var now = DateTime.Now;

                // Lấy danh sách quiz đã hết hạn
                var expiredQuizIds = await db.Quizzes
                    .Where(q => q.DueTime < now && q.IsPublished)
                    .Select(q => q.Id)
                    .ToListAsync();

                foreach (string s in expiredQuizIds)
                {
                    Console.WriteLine(s);
                }

                if (expiredQuizIds.Count == 0) return;

                // Tìm các AttemptQuiz chưa nộp
                var unsubmittedAttempts = await db.AttemptQuizzes
                    .Where(aq => expiredQuizIds.Contains(aq.QuizId) && !aq.IsSubmitted)
                    .ToListAsync();

                if (unsubmittedAttempts.Count > 0)
                {
                    foreach (var attempt in unsubmittedAttempts)
                    {
                        attempt.IsSubmitted = true;
                        await db.SaveChangesAsync();
                    }
                }
            }
        }
    }
}