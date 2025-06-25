package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequestDTO
{
    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("newPassword")
    private String newPassword;

    public ResetPasswordRequestDTO(String email, String token, String newPassword) {
        this.email = email;
        this.token = token;
        this.newPassword = newPassword;
    }

}
