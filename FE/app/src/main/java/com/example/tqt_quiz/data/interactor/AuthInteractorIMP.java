package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.Token.RetrofitClient;
import com.example.tqt_quiz.data.repository.Token.TokenManager;
import com.example.tqt_quiz.domain.APIService.LoginService;
import com.example.tqt_quiz.domain.APIService.RegService;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.AuthenInfo;
import com.example.tqt_quiz.domain.dto.AccountResponse;
import com.example.tqt_quiz.domain.dto.RegisterInfo;
import com.example.tqt_quiz.domain.interactor.AuthInteract;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthInteractorIMP implements AuthInteract {
    @Override
    public void Login(String Email, String PassWord, Context context, LoginCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        LoginService service= RetrofitClient.GetClient(tokenManager).create(LoginService.class);
        Call<AccountResponse> call=service.login(new AuthenInfo(Email,PassWord));
        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if(response.isSuccessful())
                {
                    tokenManager.SaveToken(response.body().getToken());
                    callBack.onSuccess(response.body());
                }
                else {
                    String rawJson = "";
                    try {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString("message");
                        callBack.onUnAuthorized(msg);
                        Log.e("LOGIN",msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("LOGIN","Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                callBack.FailedByNotResponse();
            }
        });
    }
    @Override
    public void Register(RegisterInfo info,Context context,RegCallBack callBack)
    {
        TokenManager tokenManager=new TokenManager(context);
        RegService service=RetrofitClient.GetClient(tokenManager).create(RegService.class);
        Call<AccountInfo>call=service.register(info);
        call.enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess();
                }
                else {
                    String rawJson = "";
                    try {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString("message");;
                        callBack.onFailedRegister(msg);
                        Log.e("Register",msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Register","Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                callBack.FailedByNotResponse();
            }
        });
    }
}
