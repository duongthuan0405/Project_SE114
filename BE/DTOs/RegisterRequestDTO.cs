namespace BE.DTOs
{
    public class RegisterRequestDTO
    {
        public string Email { get; set; } = "";
        public string Password { get; set; } = "";
        public string? FirstName { get; set; } = "";
        public string? LastMiddleName { get; set; } = "";
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