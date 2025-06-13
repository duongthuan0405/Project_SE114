using System.ComponentModel.DataAnnotations;

namespace BE.DTOs
{
    public class JoinCourseResponseDTO
    {
        [Required]
        public string CourseId { get; set; } = "";

        [Required]
        public string AccountId { get; set; } = "";

        [Required]
        public string State { get; set; } = "";

        public JoinCourseResponseDTO(string courseId, string accountId, string state)
        {
            CourseId = courseId;
            AccountId = accountId;
            State = state;
        }

        public JoinCourseResponseDTO()
        { }

    }
}
