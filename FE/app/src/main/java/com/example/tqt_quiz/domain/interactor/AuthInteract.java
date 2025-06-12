package com.example.tqt_quiz.domain.interactor;

import com.example.tqt_quiz.data.repository.Token.TokenManager;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.RegisterInfo;

public interface AuthInteract {
    public void Login(String Email, String PassWord, TokenManager tokenManager, LoginCallBack callBack);
    public interface LoginCallBack
    {
        public void onSuccess(LoginResponse response);
        public void onUnAuthorized();
        public void FailedByNotResponse();
    }
    public void Register(RegisterInfo info, TokenManager tokenManager,RegCallBack callBack);
    public interface RegCallBack
    {
        public void onSuccess();
        public void onFailedRegister();
        public void FailedByNotResponse();
    }

}
