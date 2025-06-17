package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.LoginRequest;
import com.example.tqt_quiz.domain.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/tqtquiz/authen/login")
    Call<LoginResponse> login(@Body LoginRequest infologin);
}
