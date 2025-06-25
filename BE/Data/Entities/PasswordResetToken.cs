using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("PasswordResetToken")]
    public class PasswordResetToken
    {
        [Key]
        public string Id { get; set; } = "";
        public string Email { get; set; } = "";
        public string Token { get; set; } = "";
        public DateTime ExpiredAt { get; set; }
    }
}