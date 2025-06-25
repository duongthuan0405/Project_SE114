package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface ForgotPasswordContract
{
    public interface IView
    {

        Context getTheContext();

        void navigateToResetPassword();

        void showMessage(String msg);
    }

    public interface IPresenter
    {

        void onClickContinue(String string);
    }
}
