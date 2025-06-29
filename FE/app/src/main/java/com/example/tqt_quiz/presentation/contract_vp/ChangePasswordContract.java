package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface ChangePasswordContract
{
    public interface IView
    {

        Context getTheContext();

        void onSuccess();
        void navigateToLogin();
        void showMessage(String msg);
    }
    
    public interface IPresenter
    {

        void changePassword(String newPass, String oldPass);

        void Logout();
    }
}
