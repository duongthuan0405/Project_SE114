package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.UpdatePasswordService;
import com.example.tqt_quiz.domain.dto.UpdatePasswordDTO;
import com.example.tqt_quiz.domain.interactor.UpdatePasswordInteractor;
import com.example.tqt_quiz.presentation.view.activities.Login;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordInteractorIMP implements UpdatePasswordInteractor {
    @Override
    public void Updatepassword(UpdatePasswordDTO info, Context context, UpdatepasswordCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        UpdatePasswordService service= RetrofitClient.GetClient(tokenManager).create(UpdatePasswordService.class);
        Call<Void> call= service.UpdatePassword(info);
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
