namespace BE.DTOs
{
    public class CourseDTO
    {

        public string Id { get; set; }
        public string Name { get; set; }
        public string HostName { get; set; }
        public string? Avatar { get; set; }

        public CourseDTO(string id, string name, string hostName, string avatar)
        {
            Id = id;
            Name = name;
            HostName = hostName;
            Avatar = avatar;
        }

        public CourseDTO() { }

    }
}