package com.example.tqt_quiz.presentation.contract_vp;

import android.accounts.Account;

public interface LoginContract {
    public interface IView{

        void loginSuccess();

        void loginFailed();
    }

    public interface IPresenter{

        void LoginClick(Account account);
    }
}
