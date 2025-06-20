namespace BE.DTOs
{
    public class AnswerDTO
    {
        public string Id { get; set; } = "";
        public string Content { get; set; } = "";
        public bool IsCorrect { get; set; } = false;
        public bool IsSelected { get; set; } = false;
        public AnswerDTO() { }
        public AnswerDTO(string id, string content, bool isCorrect, bool isSelected)
        {
            Id = id;
            Content = content;
            IsCorrect = isCorrect;
            IsSelected = isSelected;
        }
    }
}
