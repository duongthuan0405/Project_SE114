package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AuthenInfo;
import com.example.tqt_quiz.domain.dto.AccountResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/tqtquiz/authen/login")
    Call<AccountResponse> login(@Body AuthenInfo infologin);
}
