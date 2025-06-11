package com.example.tqt_quiz.presentation.presenter;

import android.accounts.Account;

import com.example.tqt_quiz.domain.interactor.IMainInteractor;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;

public class LoginPresenter implements LoginContract.IPresenter {
    LoginContract.IView view;

    public LoginPresenter(LoginContract.IView view) {
        this.view = view;
    }

    @Override
    public void LoginClick(Account account) {
        if (account.isValidEmail() && account.isValidPw()){
            view.loginSuccess();
        } else {
            view.loginFailed();
        }
    }

    @Override
    public void RegisterClick() {
        view.navigateToRegister();
    }
}
