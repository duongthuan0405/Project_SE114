using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace BE.DTOs
{
    public class LoginRequestDTO
    {
        [Required]
        public string Email { get; set; } = "";

        [Required]
        public string Password { get; set; } = "";
        public LoginRequestDTO(string email, string password)
        {
            Email = email;
            Password = password;
        }
        public LoginRequestDTO() { }
        
    }
}