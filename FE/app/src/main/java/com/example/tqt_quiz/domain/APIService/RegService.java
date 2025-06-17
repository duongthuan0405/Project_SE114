package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegService {
    @POST("/tqtquiz/Authen/register")
    Call<AccountInfo> register(@Body RegisterRequest registerInfo);
}
