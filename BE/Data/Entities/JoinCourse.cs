using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("JoinCourse")]
    public class JoinCourse
    {
        // [Key] ====
        public string CourseID { get; set; }
        public string AccountID { get; set; }
        public DateTime TimeJoin { get; set; }
        // =====
        public int State { get; set; }

        [ForeignKey("CourseID")]
        [Required]
        public Course OCourse { get; set; }

        [ForeignKey("AccountID")]
        [Required]
        public Account OAccount { get; set; }

        public enum JoinCourseState
        {
            Pending = 0,
            Denied = 1,
            Joined = 2,
            Left = 3
        }

    }
}