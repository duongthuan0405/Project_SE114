package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.ForgotPassService;
import com.example.tqt_quiz.domain.APIService.LoginService;
import com.example.tqt_quiz.domain.APIService.RegService;
import com.example.tqt_quiz.domain.APIService.ResetPasswordService;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.ForgotPasswordRequestDTO;
import com.example.tqt_quiz.domain.dto.LoginRequest;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.RegisterRequest;
import com.example.tqt_quiz.domain.dto.ResetPasswordRequestDTO;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthInteractorIMP implements IAuthInteract {
    @Override
    public void Login(String Email, String PassWord, Context context, IAuthInteract.LoginCallBack callBack) {
        TokenManager tokenManager = new TokenManager(context);
        LoginService service= RetrofitClient.GetClient(tokenManager).create(LoginService.class);
        Call<LoginResponse> call=service.login(new LoginRequest(Email,PassWord));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful())
                {
                    tokenManager.SaveToken(response.body().getToken());
                    // Lưu roleId tại đây
                    callBack.onSuccess(response.body());
                }
                else {
                    String rawJson = "";
                    try {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString("message");
                        callBack.onUnAuthorized(msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.FailedByNotResponse("Không thể kết nối đến server");
            }
        });
    }
    @Override
    public void Register(RegisterRequest info, Context context, RegCallBack callBack)
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
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                callBack.FailedByNotResponse("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void ForgotPassword(ForgotPasswordRequestDTO dto, Context context, ForgotPasswordCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        ForgotPassService service=RetrofitClient.GetClient(tokenManager).create(ForgotPassService.class);
        Call<Void> call = service.forgotPassword(dto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                    callBack.onSuccess();
                else
                {
                    String rawJson = "";
                    try {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString("message");;
                        callBack.onOtherFailure(msg);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                callBack.onFailureByCannotConnectToServer("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void ResetPassword(ResetPasswordRequestDTO dto, Context context, ResetPasswordCallBack callBack) {
        TokenManager tokenManager = new TokenManager(context);
        ResetPasswordService service = RetrofitClient.GetClient(tokenManager).create(ResetPasswordService.class);
        Call<Void> call = service.resetPassword(dto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess();
                } else {
                    String rawJson = "";
                    try {
                        rawJson = response.errorBody().string();
                        JSONObject obj = new JSONObject(rawJson);
                        String msg = obj.optString("message");
                        callBack.onOtherFailure(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailureByCannotConnectToServer();
            }
        });
    }
}
