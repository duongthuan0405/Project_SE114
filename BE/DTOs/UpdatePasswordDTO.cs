using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class UpdatePasswordDTO
    {
        public string OldPassword { get; set; } = "";
        public string NewPassword { get; set; } = "";
    
    }
}