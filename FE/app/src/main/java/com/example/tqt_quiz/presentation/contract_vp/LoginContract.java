package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface LoginContract {
    public interface IView{

        void loginSuccess(String role);
        void showLoginError(String message);
        void navigateToRegister();
        void navigateToForgotPassword();

        Context getContext();
    }

    public interface IPresenter{
        void LoginClick(String email, String password);
        void RegisterClick();
        void ForgotPasswordClick();
    }
}
