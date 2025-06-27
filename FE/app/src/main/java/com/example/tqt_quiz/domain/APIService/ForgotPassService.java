package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.ForgotPasswordRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ForgotPassService
{
    @POST("/tqtquiz/Authen/forgot-password")
    Call<Void> forgotPassword(@Body ForgotPasswordRequestDTO request);
}
