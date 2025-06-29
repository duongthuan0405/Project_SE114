package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountInfo;

public interface ProfileFragmentContract
{
    public interface IView
    {

        Context getTheContext();

        void navigationToLogin();


        void showInfo(AccountInfo response);

        void showError(String msg);

        void onGetCurrentInfoSuccess(AccountInfo response);
    }

    public interface IPresenter
    {
        void onLogoutClick();

        void getMySelfAccountInfo();

        void getProfile();

        void showCurrentInfo();
    }
}
