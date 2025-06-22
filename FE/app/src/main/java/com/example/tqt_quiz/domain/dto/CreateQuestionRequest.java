package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateQuestionRequest {
    @SerializedName("content")
    String content;
    @SerializedName("quizId")
    String quizId;
    @SerializedName("lAnswers")
    List<CreateAnswerRequest> lAnswers;
    public List<CreateAnswerRequest> getLAnswers() {
        return lAnswers;
    }

    public void setLAnswers(List<CreateAnswerRequest> lAnswers) {
        this.lAnswers = lAnswers;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
