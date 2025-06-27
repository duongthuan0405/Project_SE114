package com.example.tqt_quiz.data.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.UpdateAccountInfoService;
import com.example.tqt_quiz.domain.dto.UpdateAccountInfoDTO;
import com.example.tqt_quiz.domain.interactor.IAccountUpdateInteractor;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccountInteractorIMP implements IAccountUpdateInteractor
{

    @Override
    public void UpdateAccountInfo(UpdateAccountInfoDTO info, Context context, UpdateAccountInfoCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        UpdateAccountInfoService service= RetrofitClient.GetClient(tokenManager).create(UpdateAccountInfoService.class);
        Call<Void> call= service.UpdateAccountInfo(info);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess();
                }
                else
                {
                    String rawJson = "";
                    try
                    {
                        int code = response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callback.onFailureByExpiredToken();
                        else if(response.code() == 403)
                            callback.onFailureByUnAcceptedRole();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }
}
