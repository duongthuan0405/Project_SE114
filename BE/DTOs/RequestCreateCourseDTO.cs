using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace BE.DTOs
{
    public class RequestCreateCourseDTO
    {
        public string Name { get; set; }

        public string? Description { get; set; }

        public bool IsPrivate { get; set; } = true;

        public string? Avatar { get; set; }

        public string? HostId { get; set; }

        public RequestCreateCourseDTO(string name, string? description, bool isPrivate, string? avatar, string? hostId)
        {
            Name = name;
            Description = description;
            IsPrivate = isPrivate;
            Avatar = avatar;
            HostId = hostId;
        }

        public RequestCreateCourseDTO()
        {
        }
    }
}