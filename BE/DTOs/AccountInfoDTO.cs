using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BE.DTOs
{
    public class AccountInfoDTO
    {
        public string Id { get; set; } = "";
        public string Email { get; set; } = "";
        public string? FullName { get; set; } = "";

        public string? Avatar { get; set; } = "";
        public string AccountType { get; set; } = "";

        public AccountInfoDTO(string userId, string email, string? fullName, string? avatar, string accountType)
        {
            Id = userId;
            Email = email;
            FullName = fullName;
            Avatar = avatar;
            AccountType = accountType;
        }

        public AccountInfoDTO() { }

    }
}
