package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AccountInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AccountInfoService
{
    @GET("/tqtquiz/AccountInfo/{account_id}")
    public Call<AccountInfo> getAccountInfoById(@Path("account_id") String account_id);

    @GET("/tqtquiz/AccountInfo/myself")
    public Call<AccountInfo> getAccountMySelf();

}
