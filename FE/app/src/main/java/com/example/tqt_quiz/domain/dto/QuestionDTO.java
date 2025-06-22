package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionDTO {
    @SerializedName("id")
    String id;
    @SerializedName("content")
    String content;
    @SerializedName("quizId")
    String quizId;
    @SerializedName("lAnswers")
    List<AnswerDTO> lAnswers;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<AnswerDTO> getLAnswers() {
        return lAnswers;
    }

    public void setLAnswers(List<AnswerDTO> lAnswers) {
        this.lAnswers = lAnswers;
    }
}
