package com.example.tqt_quiz.presentation.presenter;

import android.accounts.Account;
import android.text.TextUtils;

import com.example.tqt_quiz.domain.interactor.IMainInteractor;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;

public class LoginPresenter implements LoginContract.IPresenter {
    LoginContract.IView view;

    public LoginPresenter(LoginContract.IView view) {
        this.view = view;
    }

    @Override
    public void LoginClick(Account account) {
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            view.showLoginError("Vui lòng điền đầy đủ thông tin");
//            return;
//        }
//
//        for (Account acc : account) {
//            if (acc.getEmail().equals(email) && acc.getPassword().equals(password)) {
//                view.loginSuccess(acc);
//                return;
//            }
//        }
//
//        view.showLoginError("Email hoặc mật khẩu không đúng");
    }

    @Override
    public void RegisterClick() {
        view.navigateToRegister();
    }

    @Override
    public void ForgotPasswordClick() {
        view.navigateToForgotPassword();
    }
}
