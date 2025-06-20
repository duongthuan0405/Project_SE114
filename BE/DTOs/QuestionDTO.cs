namespace BE.DTOs
{
    public class QuestionDTO
    {
        public string Id { get; set; } = "";
        public string Content { get; set; } = "";
        public string? QuizId { get; set; } = "";
        public List<AnswerDTO> LAnswers { get; set; } = new List<AnswerDTO>();
        public QuestionDTO(string id, string content, string quizId, List<AnswerDTO> lAnswers)
        {
            Id = id;
            Content = content;
            QuizId = quizId;
            LAnswers = lAnswers;
        }
        public QuestionDTO() { }
       
    }
}
