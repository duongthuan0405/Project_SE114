namespace BE.DTOs
{
    public class CreateQuestionRequest
    {
        public string Content { get; set; } = "";
        public string? QuizId { get; set; } = "";
        public List<CreateAnswerRequest> LAnswers { get; set; } = new List<CreateAnswerRequest>();
    }
}