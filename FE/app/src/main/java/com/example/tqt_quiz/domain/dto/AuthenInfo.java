package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class AuthenInfo {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    // Constructor (optional)
    public AuthenInfo(String email,String password) {
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

