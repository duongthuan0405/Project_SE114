using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class ForgotPwDTO
    {
        public string Email { get; set; } = string.Empty;
        public string ClientUrl { get; set; } = string.Empty;
    }
}