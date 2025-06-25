package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface RegisterContract {
    public interface IView{
        void returnLogin();
        void showRegisterSuccess();
        void showRegisterError(String message);

        Context getTheContext();
    }

    public interface IPresenter{
        void handleRegister(String type, String lastName, String firstName, String email, String password, String confirmPassword, String accountType);
    }
}
