using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using BE.ConstanctValue;
using BE.Data.Entities;
using Microsoft.EntityFrameworkCore;

namespace BE.Data.Database
{
    public class MyAppDBContext : DbContext
    {
        private string TQT_Quiz_Connection = "Server=.;Database=TQT_Quiz_DB;Trusted_Connection=True;Encrypt=False;TrustServerCertificate=True;";
        public MyAppDBContext(DbContextOptions<MyAppDBContext> options) : base(options)
        {

        }
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseSqlServer(TQT_Quiz_Connection);
            }

        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<JoinCourse>(
                b =>
                {
                    b.HasKey(jc => new { jc.AccountID, jc.CourseID, jc.TimeJoin });
                }
            );

            modelBuilder.Entity<DetailResult>(
                b =>
                {
                    b.HasKey(dr => new { dr.AnswerId, dr.AttemptQuizId });
                }
            );

            modelBuilder.Entity<AccountType>().HasData(
                new AccountType { Id = "0000000000", Name = "Quản trị" },
                new AccountType { Id = "0000000001", Name = "Giáo viên" },
                new AccountType { Id = "0000000002", Name = "Học sinh" }
            );

        }

        public DbSet<AccountType> AccountTypes { get; set; }
        public DbSet<Account> Accounts { get; set; }
        public DbSet<AccountAuthen> AccountAuthens { get; set; }
        public DbSet<Course> Courses { get; set; }
        public DbSet<JoinCourse> JoinCourses { get; set; }
        public DbSet<Quiz> Quizzes { get; set; }
        public DbSet<Question> Questions { get; set; }
        public DbSet<Answer> Answers { get; set; }
        public DbSet<AttemptQuiz> AttemptQuizzes { get; set; }
        public DbSet<DetailResult> DetailResults { get; set; }
        public DbSet<PasswordResetToken> PasswordResetTokens { get; set; }
        
    }
}