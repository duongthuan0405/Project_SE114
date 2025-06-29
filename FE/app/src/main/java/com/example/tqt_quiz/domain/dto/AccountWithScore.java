package com.example.tqt_quiz.domain.dto;

import com.example.tqt_quiz.domain.JasonAdapters.LocalDateTimeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class AccountWithScore {
    @SerializedName("account")
    AccountInfo account;
    @SerializedName("totalCorrectAnswer")
    int totalCorrectAnswer;
    @SerializedName("totalQuestions")
    int totalQuestions;
    @SerializedName("isSubmitted")
    boolean isSubmitted;

    @SerializedName("startedAt")@JsonAdapter(LocalDateTimeAdapter.class)
    LocalDateTime startAt;

    @SerializedName("finishedAt")@JsonAdapter(LocalDateTimeAdapter.class)
    LocalDateTime finishAt;
    public AccountInfo getAccount() {
        return account;
    }

    public void setAccount(AccountInfo account) {
        this.account = account;
    }

    public int getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public void setTotalCorrectAnswer(int totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean submitted) {
        isSubmitted = submitted;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(LocalDateTime finishAt) {
        this.finishAt = finishAt;
    }
}
