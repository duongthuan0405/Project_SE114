using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BE.Data.Database;
using BE.Data.Entities;
using Microsoft.EntityFrameworkCore;

namespace BE.AutoService
{
    public class AutoDeleteResetPassToken : BackgroundService
    {
        private readonly IServiceProvider _serviceProvider;
        private readonly TimeSpan _interval = TimeSpan.FromMinutes(30);
        public AutoDeleteResetPassToken(IServiceProvider serviceProvider)
        {
            _serviceProvider = serviceProvider;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                await AutoDeleteAsync();
                await Task.Delay(_interval, stoppingToken);
            }
        }

        private async Task AutoDeleteAsync()
        {
            using (var scope = _serviceProvider.CreateScope())
            {
                var db = scope.ServiceProvider.GetRequiredService<MyAppDBContext>();
                var now = DateTime.Now;

                var tokens = await db.PasswordResetTokens.Where(p => p.ExpiredAt < now).ToListAsync();
                db.PasswordResetTokens.RemoveRange(tokens);

                await db.SaveChangesAsync();
            }
        }
    }
}