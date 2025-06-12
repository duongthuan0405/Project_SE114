namespace BE.DTOs
{
    public class CourseDTO
    {
        public string Id { get; set; } = "";
        public string Name { get; set; } = "";
        public string HostName { get; set; } = "";
        public bool IsPrivate { get; set; } = true;
        public string? Avatar { get; set; } = "";
        public string? Description { get;  set; } = "";

        public CourseDTO(string id, string name, string hostName, bool isPrivate, string? avatar, string? description)
        {
            Id = id;
            Name = name;
            HostName = hostName;
            IsPrivate = isPrivate;
            Avatar = avatar;
            Description = description;
        }

        public CourseDTO() { }

    }
}