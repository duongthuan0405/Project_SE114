namespace BE.DTOs
{
    public class LoginResponseDTO
    {
        public string Message { get; set; }
        public string? Token { get; set; }
        public DateTime? Expires { get; set; }
        public string? UserId { get; set; }
        public string? FullName { get; set; }
        public string? Email { get; set; }
        public string? Role { get; set; }
        public LoginResponseDTO(string message, string token, DateTime expires, string userId, string fullName, string email, string role)
        {
            Message = message;
            Token = token;
            Expires = expires;
            UserId = userId;
            FullName = fullName;
            Email = email;
            Role = role;
        }

        public LoginResponseDTO() { }
    }
}
