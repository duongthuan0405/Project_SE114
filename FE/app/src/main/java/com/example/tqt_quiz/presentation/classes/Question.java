package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private int id;
    private int quizId;
    private String content;
    private List<Answer> answers;

    public Question(String content, List<Answer> answers) {
        this.content = content;
        this.answers = answers;
    }

    public Question(int id, int quizId, String content, List<Answer> answers) {
        this(content, answers);
        this.id = id;
        this.quizId = quizId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
