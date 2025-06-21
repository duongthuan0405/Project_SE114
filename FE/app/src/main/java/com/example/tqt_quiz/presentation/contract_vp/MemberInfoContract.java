package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountInfo;

public interface MemberInfoContract
{
    public interface IView
    {

        Context getTheContext();

        void showInfo(AccountInfo response);

        void navigateToLogin();

        void showToast(String s);
    }

    public interface IPresenter
    {

        void showInfo(String memberId);
    }
}
