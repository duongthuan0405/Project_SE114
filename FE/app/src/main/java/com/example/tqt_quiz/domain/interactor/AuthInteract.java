package com.example.tqt_quiz.domain.interactor;

import com.example.tqt_quiz.data.repository.Token.TokenManager;
import com.example.tqt_quiz.domain.dto.LoginResponse;

public interface AuthInteract {
    public void Login(String Email, String PassWord, TokenManager tokenManager, AuthCallBack callBack);
    public interface AuthCallBack
    {
        public void onSuccess(LoginResponse response);
        public void onUnAuthorized();
        public void FailedByNotResponse();
    }
    //public void Register(AuthCallBack callBack);
}
