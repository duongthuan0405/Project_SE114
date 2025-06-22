package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String id;
    private String quizId;
    private String content;
    private List<Answer> answers;

    public Question(String content, List<Answer> answers) {
        this.content = content;
        this.answers = answers;
    }

    public Question(String id, String quizId, String content, List<Answer> answers) {
        this(content, answers);
        this.id = id;
        this.quizId = quizId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
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
