using System.ComponentModel.DataAnnotations;
using BE.ConstanctValue;

namespace BE.DTOs
{
    public class RegisterRequestDTO
    {
        [Required]
        public string Email { get; set; } = "";

        [Required]
        public string Password { get; set; } = "";

        public string? FirstName { get; set; } = "";

        public string? LastMiddleName { get; set; } = "";
        public string AccountTypeId { get; set; } = StaticClass.RoleId.Student;

        public RegisterRequestDTO(string email, string password, string firstName, string lastMiddleName, string accountTypeId = StaticClass.RoleId.Student)
        {
            Email = email;
            Password = password;
            FirstName = firstName;
            LastMiddleName = lastMiddleName;
            AccountTypeId = accountTypeId;
        }
        public RegisterRequestDTO()
        {
        }
    }
}