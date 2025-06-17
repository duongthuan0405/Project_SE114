using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BE.DTOs
{
    public class AccountInfoDTO
    {
        [Required]
        public string Id { get; set; } = "";

        [Required]
        public string Email { get; set; } = "";

        public string? FullName { get; set; } = "";

        public string? Avatar { get; set; } = "";

        [Required]
        public string AccountType { get; set; } = "";

        [Required]
        public string AccountTypeId { get; set; } = "";

        public AccountInfoDTO(string userId, string email, string? fullName, string? avatar, string accountTypeId, string accountType)
        {
            Id = userId;
            Email = email;
            FullName = fullName;
            Avatar = avatar;
            AccountType = accountType;
            AccountTypeId = accountTypeId;
        }

        public AccountInfoDTO() { }

    }
}
