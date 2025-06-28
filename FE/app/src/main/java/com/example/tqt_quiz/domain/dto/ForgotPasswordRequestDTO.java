package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequestDTO
{
    @SerializedName("email")
    private String email;

    @SerializedName("clientUrl")
    private String clientUrl;

    public ForgotPasswordRequestDTO(String email) {
        this.email = email;
        clientUrl = "tqtquiz://reset-password";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }
}
