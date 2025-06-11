namespace BE.DTOs
{
    public class RegisterResponseDTO
    {
        public string UserId { get; set; } = "";

        public RegisterResponseDTO(string userId)
        {
            UserId = userId;
        }
        public RegisterResponseDTO() { }
    }
}
