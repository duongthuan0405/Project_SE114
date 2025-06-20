using BE.ConstanctValue;
using System.ComponentModel.DataAnnotations;

namespace BE.DTOs
{
    public class CourseDTO
    {
        [Required]
        public string Id { get; set; } = "";

        [Required]
        public string Name { get; set; } = "";

        public string? HostName { get; set; } = "";
        public string? HostId { get; set; } = "";

        [Required]
        public bool IsPrivate { get; set; } = true;

        public string? Avatar { get; set; } = "";

        public string? Description { get;  set; } = "";
        public string? StateOfJoining { get; set; } = "";

        public CourseDTO(string id, string name, string hostId, string hostName, bool isPrivate, string? avatar, string? description, string stateOfJoinCourse)
        {
            Id = id;
            Name = name;
            HostName = hostName;
            IsPrivate = isPrivate;
            Avatar = avatar;
            Description = description;
            HostId = hostId;
            StateOfJoining = stateOfJoinCourse;
        }

        public CourseDTO() { }

    }
}