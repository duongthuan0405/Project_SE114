namespace BE.DTOs
{
    public class RegisterResponseDTO
    {
        public string Message { get; set; }
        public string? UserId { get; set; }

        public RegisterResponseDTO(string message, string? userId)
        {
            Message = message;
            UserId = userId;
        }
        public RegisterResponseDTO() { }
    }
}
