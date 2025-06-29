package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.UpdatePasswordDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;

public interface UpdatePasswordService
{
    @PATCH("/tqtquiz/Authen/update-password")
    Call<Void> UpdatePassword(@Body UpdatePasswordDTO info);
}
