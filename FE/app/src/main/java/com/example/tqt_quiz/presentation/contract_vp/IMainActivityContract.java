package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface IMainActivityContract {
    public interface IView
    {

        Context getTheContext();

        void navigateToMainHomeForTeacher();

        void navigateToLogin();

        void showToast(String msg);

        void navigateToMainHomeForStudent();

        void navigateToMainHomeForAdmin();
    }
    
    public interface IPresenter
    {

        void onDecideToNavigate();
    }
}
