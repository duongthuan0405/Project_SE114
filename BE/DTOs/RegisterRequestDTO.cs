using System.ComponentModel.DataAnnotations;

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

        [Required]
        public string AccountTypeId { get; set; } = "";
        public RegisterRequestDTO(string email, string password, string firstName, string lastMiddleName, string accountTypeId)
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