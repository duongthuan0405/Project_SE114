package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.ResetPasswordRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ResetPasswordService
{
    @POST("/tqtquiz/Authen/reset-password")
    Call<Void> resetPassword(@Body ResetPasswordRequestDTO request);
}
