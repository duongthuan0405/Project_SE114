using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("Answer")]
    public class Answer
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "NVarChar(1000)")]
        [MaxLength(1000)]
        public string Content { get; set; }

        [Required]
        [Column(TypeName = "Bit")]
        public bool IsTrue { get; set; }

        [Required]
        public string QuestionID { get; set; }

        [ForeignKey("QuestionID")]
        [Required]
        public Question OQuestion { get; set; }
        public List<DetailResult> LDetailResults { get; set; } = new List<DetailResult>();
    }
}