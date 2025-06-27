package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.ResetPasswordService;
import com.example.tqt_quiz.domain.dto.ForgotPasswordRequestDTO;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.RegisterRequest;
import com.example.tqt_quiz.domain.dto.ResetPasswordRequestDTO;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface IAuthInteract
{
    public void Login(String Email, String PassWord, Context context, LoginCallBack callBack);
    public interface LoginCallBack
    {
        public void onSuccess(LoginResponse response);
        public void onUnAuthorized(String msg);
        public void FailedByNotResponse(String msg);

    }
    public void Register(RegisterRequest info, Context context, RegCallBack callBack);
    public interface RegCallBack
    {
        public void onSuccess();
        public void onFailedRegister(String msg);
        public void FailedByNotResponse(String msg);
    }

    public void ForgotPassword(ForgotPasswordRequestDTO dto, Context context, ForgotPasswordCallBack callBack);
    public interface ForgotPasswordCallBack
    {
        public void onSuccess();
        public void onOtherFailure(String msg);
        public void onFailureByCannotConnectToServer(String msg);
    }

    public void ResetPassword(ResetPasswordRequestDTO dto, Context context, ResetPasswordCallBack callBack);
                
    public interface ResetPasswordCallBack {
        
        void onSuccess();

        void onOtherFailure(String msg);

        void onFailureByCannotConnectToServer();
    }
}
