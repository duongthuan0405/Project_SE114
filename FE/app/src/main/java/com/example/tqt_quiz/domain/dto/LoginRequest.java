package com.example.tqt_quiz.domain.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    @NonNull
    private String email;

    @SerializedName("password")
    @NonNull
    private String password;

    // Constructor (optional)
    public LoginRequest(String email, String password) {
        this.email=email;
        this.password=password;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}

