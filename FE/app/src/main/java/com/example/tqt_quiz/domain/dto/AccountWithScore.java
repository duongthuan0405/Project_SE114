package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class AccountWithScore {
    @SerializedName("account")
    AccountInfo account;
    @SerializedName("totalCorrectAnswer")
    int totalCorrectAnswer;
    @SerializedName("totalQuestions")
    int totalQuestions;
    @SerializedName("isSubmitted")
    boolean isSubmitted;
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
}
