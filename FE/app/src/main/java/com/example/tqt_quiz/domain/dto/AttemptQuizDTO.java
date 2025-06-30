package com.example.tqt_quiz.domain.dto;

import com.example.tqt_quiz.domain.JasonAdapters.LocalDateTimeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class AttemptQuizDTO {
    @SerializedName("id")
    String id;
    @SerializedName("quizId")
    String quizId;
    @SerializedName("accountId")
    String accountId;
    @SerializedName("quizName")
    String QuizName;
    @SerializedName("courseName")
    String CourseName;
    @SerializedName("attemptTime")
            @JsonAdapter(LocalDateTimeAdapter.class)
    LocalDateTime attemptTime;
    @SerializedName("finishTime")
    @JsonAdapter(LocalDateTimeAdapter.class)
    LocalDateTime finishTime;
    @SerializedName("isSubmitted")
    boolean isSubmitted;
    @SerializedName("tokenForQuiz")
    String tokenForQuiz;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(LocalDateTime attemptTime) {
        this.attemptTime = attemptTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }
    public String getQuizName() {
        return QuizName;
    }

    public void setQuizName(String quizName) {
        this.QuizName = quizName;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }
    public String getTokenForQuiz() {
        return tokenForQuiz;
    }

    public void setTokenForQuiz(String tokenForQuiz) {
        this.tokenForQuiz = tokenForQuiz;
    }
}
