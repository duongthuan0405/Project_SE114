namespace BE.DTOs
{
    public class CreateAnswerRequest
    {
        public string Content { get; set; } = "";
        public bool IsCorrect { get; set; } = false;

    }
}
