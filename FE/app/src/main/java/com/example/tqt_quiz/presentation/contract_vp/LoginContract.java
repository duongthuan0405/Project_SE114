package com.example.tqt_quiz.presentation.contract_vp;

import android.accounts.Account;

public interface LoginContract {
    public interface IView{

        void loginSuccess();

        void showLoginError(String message);
        void navigateToRegister();
        void navigateToForgotPassword();
    }

    public interface IPresenter{
        void LoginClick(Account account);
        void RegisterClick();
        void ForgotPasswordClick();
    }
}
