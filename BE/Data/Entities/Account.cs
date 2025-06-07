using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using Microsoft.Extensions.Logging.Abstractions;

namespace BE.Data.Entities
{
    [Table("Account")]
    public class Account
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Column(TypeName = "NVarChar(40)")]
        [MaxLength(40)]
        public string? LastMiddleName { get; set; }

        [Column(TypeName = "NVarChar(20)")]
        [MaxLength(20)]
        public string? FirstName { get; set; }

        [Column(TypeName = "VarChar(3000)")]
        [MaxLength(3000)]
        public string? Avatar { get; set; }

        [Required]
        public string AccountTypeId { get; set; }

        [ForeignKey("AccountTypeId")]
        [Required]
        public AccountType OAccountType { get; set; }

        [ForeignKey("Id")]
        [Required]
        public AccountAuthen OAccountAuthen { get; set; }
        public List<Course> LOwnCourses { get; set; } = new List<Course>();
        public List<JoinCourse> LJoinCourses { get; set; } = new List<JoinCourse>();
        public List<AttemptQuiz> LAttemptQuizzes { get; set; } = new List<AttemptQuiz>();
   
    }
}