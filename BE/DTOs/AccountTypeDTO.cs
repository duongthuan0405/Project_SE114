using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class AccountTypeDTO
    {
        public AccountTypeDTO(string name)
        {
            Name = name;
        }
        public AccountTypeDTO() { }

        public string Name { get; set; } = "";

    }
}