package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.RegisterRequest;

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
}
