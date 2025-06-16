package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

public interface ProfileFragmentContract
{
    public interface IView
    {

        Context getTheContext();

        void navigationToLogin();


    }

    public interface IPresenter
    {
        void onLogoutClick();
    }
}
