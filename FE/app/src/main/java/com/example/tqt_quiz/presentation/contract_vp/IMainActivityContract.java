package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface IMainActivityContract {
    public interface IView
    {

        Context getTheContext();

        void navigateToMainHome();

        void navigateToLogin();

        void showToast(String msg);
    }
    
    public interface IPresenter
    {

        void onDecideToNavigate();
    }
}
