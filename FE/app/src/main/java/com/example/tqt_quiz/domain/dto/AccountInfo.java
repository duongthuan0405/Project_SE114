package com.example.tqt_quiz.domain.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class AccountInfo {
    @SerializedName("userId")
    @NotNull
    private String UserID;

    @SerializedName("email")
    @NotNull
    private String Email;

    @SerializedName("fullname")
    private String FullName;

    @SerializedName("avatar")
    private String Avatar;

    @SerializedName("accountType")
    @NotNull
    private String AccountType;

    // Getter and Setter for UserID
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    // Getter and Setter for Email
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    // Getter and Setter for FullName
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    // Getter and Setter for Avatar
    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        this.Avatar = avatar;
    }

    // Getter and Setter for AccountType
    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        this.AccountType = accountType;
    }
}

