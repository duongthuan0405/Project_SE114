package com.example.tqt_quiz.data.interactor;

import android.util.Log;

import com.example.tqt_quiz.data.repository.Token.RetrofitClient;
import com.example.tqt_quiz.data.repository.Token.TokenManager;
import com.example.tqt_quiz.domain.APIService.LoginService;
import com.example.tqt_quiz.domain.APIService.RegService;
import com.example.tqt_quiz.domain.dto.AuthenInfo;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.RegisterInfo;
import com.example.tqt_quiz.domain.dto.RegisterResponse;
import com.example.tqt_quiz.domain.interactor.AuthInteract;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthInteractorIMP implements AuthInteract {
    @Override
    public void Login(String Email, String PassWord, TokenManager tokenManager, LoginCallBack callBack) {
        LoginService service= RetrofitClient.GetClient(tokenManager).create(LoginService.class);
        Call<LoginResponse> call=service.login(new AuthenInfo(Email,PassWord));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
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
                        callBack.onUnAuthorized();
                        Log.e("LOGIN",msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("LOGIN","Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.FailedByNotResponse();
            }
        });
    }
    @Override
    public void Register(RegisterInfo info,TokenManager tokenManager,RegCallBack callBack)
    {
        RegService service=RetrofitClient.GetClient(tokenManager).create(RegService.class);
        Call<RegisterResponse>call=service.register(info);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
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
                        callBack.onFailedRegister();
                        Log.e("Register",msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("Register","Error");
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callBack.FailedByNotResponse();
            }
        });
    }
}
