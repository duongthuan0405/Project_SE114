package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class QuizWithScoreDTO {
    @SerializedName("quiz")
    QuizDTO quiz;
    @SerializedName("totalCorrectAnswer")
    int totalCorrectAnswer;
    @SerializedName("totalQuestion")
    int totalQuestion;
    @SerializedName("isSubmitted")
    boolean isSubmitted;
    public QuizDTO getQuiz() {
        return quiz;
    }

    public void setQuiz(QuizDTO quiz) {
        this.quiz = quiz;
    }

    public int getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public void setTotalCorrectAnswer(int totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }
}
