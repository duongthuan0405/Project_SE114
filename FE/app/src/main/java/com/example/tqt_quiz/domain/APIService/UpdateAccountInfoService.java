package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.UpdateAccountInfoDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface UpdateAccountInfoService {
    @PUT("/tqtquiz/AccountInfo")
    Call<Void> UpdateAccountInfo(@Body UpdateAccountInfoDTO info);
}
