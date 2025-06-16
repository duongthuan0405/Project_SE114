using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    [Table("AccountType")]
    public class AccountType
    {
        [Key]
        [Column(TypeName = "Char(10)")]
        [MinLength(10), MaxLength(10)]
        public string Id { get; set; }

        [Required]
        [Column(TypeName = "NVarChar(20)")]
        [MaxLength(20)]
        public string Name { get; set; }
        
        public List<Account> LAccounts { get; set; } = new List<Account>();
    }
}