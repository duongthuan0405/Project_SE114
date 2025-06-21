package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Answer implements Serializable {
    private int id;
    private int questionId;
    private String content;
    private boolean isCorrect;

    public Answer(String content, boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public Answer(int id, int questionId, String content, boolean isCorrect) {
        this(content, isCorrect);
        this.id = id;
        this.questionId = questionId;
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
}
