package com.example.tqt_quiz.presentation.contract_vp;

import android.accounts.Account;

public interface RegisterContract {
    public interface IView{
        void navigateToLogin();
        void showRegisterSuccess();
        void showRegisterError(String message);
    }

    public interface IPresenter{
        void handleRegister(String type, String lastName, String firstName, String email, String password, String confirmPassword);
    }
}
