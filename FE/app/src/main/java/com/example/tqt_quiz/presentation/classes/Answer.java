package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Answer implements Serializable {
    private String id;
    private String questionId;
    private String content;
    private boolean isCorrect;
    private boolean isSelected;

    public Answer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public Answer() {
    }

    public Answer(String id, String questionId, String content, boolean isCorrect) {
        this(content, isCorrect);
        this.id = id;
        this.questionId = questionId;
    }

    public Answer(String id, String questionId, String content, boolean isCorrect, boolean isSelected) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.isCorrect = isCorrect;
        this.isSelected = isSelected;
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

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
