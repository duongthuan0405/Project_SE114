using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class LoginRequestDTO
    {
        public string Email { get; set; } = "";
        public string Password { get; set; } = "";
        public LoginRequestDTO(string email, string password)
        {
            Email = email;
            Password = password;
        }
        public LoginRequestDTO() { }
        
    }
}