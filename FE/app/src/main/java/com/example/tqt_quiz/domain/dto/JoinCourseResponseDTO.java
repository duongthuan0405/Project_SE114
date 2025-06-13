package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class JoinCourseResponseDTO {
    @SerializedName("courseid")
    @NotNull
    private String CourseID;

    @SerializedName("accountId")
    @NotNull
    private String AccountID;

    @SerializedName("state")
    @NotNull
    private String state;

    // Getter and Setter for CourseID
    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(@NotNull String courseID) {
        this.CourseID = courseID;
    }

    // Getter and Setter for AccountID
    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(@NotNull String accountID) {
        this.AccountID = accountID;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(@NotNull String state) {
        this.state = state;
    }
}
