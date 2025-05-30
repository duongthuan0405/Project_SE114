using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
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
        }

        public DbSet<MyTestEntity> MyTestEntities { get; set; }
    }
}