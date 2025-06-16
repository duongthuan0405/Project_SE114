using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace BE.Data.Entities
{
    public class MyTestEntity
    {
        public MyTestEntity(string iD, string name, int age)
        {
            ID = iD;
            Name = name;
            Age = age;
        }

        [Key]
        [MaxLength(10)]
        public string ID { get; set; }
        [Required]
        [MaxLength(20)]
        public string Name { get; set; }
        public int Age { get; set; }
        public Account Accounts { get; set; }
        
    }
}