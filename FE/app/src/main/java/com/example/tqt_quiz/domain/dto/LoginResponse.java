package com.example.tqt_quiz.domain.dto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class LoginResponse {

    @SerializedName("message")
    @NonNull
    private String message;

    @SerializedName("token")
    @Nullable
    private String token;

    @SerializedName("expries")
    @Nullable
    private LocalDateTime expires;

    @SerializedName("userid")
    @Nullable
    private String userID;

    @SerializedName("fullname")
    @Nullable
    private String fullName;

    @SerializedName("Email")
    @Nullable
    private String email;

    @SerializedName("role")
    @Nullable
    private String role;

    // Getter and Setter for message
    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    // Getter and Setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter and Setter for expires
    @Nullable
    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(@Nullable LocalDateTime expires) {
        this.expires = expires;
    }

    // Getter and Setter for userID
    @Nullable
    public String getUserID() {
        return userID;
    }

    public void setUserID(@Nullable String userID) {
        this.userID = userID;
    }

    // Getter and Setter for fullName
    @Nullable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@Nullable String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for email
    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@Nullable String email) {
        this.email = email;
    }

    // Getter and Setter for role
    @Nullable
    public String getRole() {
        return role;
    }

    public void setRole(@Nullable String role) {
        this.role = role;
    }
}

