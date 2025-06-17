using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("AccountAuthen")]
    public class AccountAuthen
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "VarChar(60)")]
        [MaxLength(60)]
        public string Email { get; set; }

        [Required]
        [Column(TypeName = "VarChar(25)")]
        [MinLength(5), MaxLength(25)]
        public string Password { get; set; }

    }
}