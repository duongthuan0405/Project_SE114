package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.RegisterInfo;
import com.example.tqt_quiz.domain.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegService {
    @POST("/tqtquiz/Authen/register")
    Call<RegisterResponse> register(@Body RegisterInfo registerInfo);
}
