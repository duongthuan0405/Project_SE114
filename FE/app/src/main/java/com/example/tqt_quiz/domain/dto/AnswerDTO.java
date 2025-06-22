package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class AnswerDTO {
    @SerializedName("id")
    String id;
    @SerializedName("content")
    String content;
    @SerializedName("isCorrect")
    boolean isCorrect;
    @SerializedName("isSelected")
    boolean isSelected;
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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
