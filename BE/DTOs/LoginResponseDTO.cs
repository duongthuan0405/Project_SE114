namespace BE.DTOs
{
    public class LoginResponseDTO
    {
        public string? Token { get; set; } = "";
        public string? UserId { get; set; } = "";
        public string? FullName { get; set; } = "";
        public string Role { get; set; } = "";
        public LoginResponseDTO(string token, DateTime expires, string userId, string fullName, string email, string role)
        {
            Token = token;
            UserId = userId;
            FullName = fullName;
            Role = role;
        }

        public LoginResponseDTO() { }
    }
}
