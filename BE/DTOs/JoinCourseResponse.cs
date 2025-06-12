namespace BE.DTOs
{
    public class JoinCourseResponse
    {
        public string CourseId { get; set; } = "";
        public string AccountId { get; set; } = "";
        public string State { get; set; } = "";

        public JoinCourseResponse(string courseId, string accountId, string state)
        {
            CourseId = courseId;
            AccountId = accountId;
            State = state;
        }

        public JoinCourseResponse()
        { }

    }
}
