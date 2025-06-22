package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class CreateAnswerRequest {
    @SerializedName("content")
    String content;
    @SerializedName("isCorrect")
    boolean isCorrect;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
}
