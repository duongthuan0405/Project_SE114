using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("Course")]
    public class Course
    {
        [Key]
        [Column(TypeName = "Char(6)")]
        [MinLength(6), MaxLength(6)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "NVarChar(20)")]
        [MaxLength(20)]
        public string Name { get; set; }

        [Column(TypeName = "NVarChar(100)")]
        [MaxLength(100)]
        public string? Description { get; set; }

        [Column(TypeName = "Bit")]
        public bool IsPrivate { get; set; } = true;

        [Column(TypeName = "VarChar(3000)")]
        [MaxLength(3000)]
        public string Avatar { get; set; }

        public string? HostId { get; set; }

        [ForeignKey("HostId")]
        public Account? OHost { get; set; }

        public List<JoinCourse> LJoinCourses { get; set; } = new List<JoinCourse>();
        public List<Quiz> LQuizes { get; set; } = new List<Quiz>();
    }
}