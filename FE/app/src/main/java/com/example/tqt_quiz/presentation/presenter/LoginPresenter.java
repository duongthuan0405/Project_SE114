package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.domain.interactor.IMainInteractor;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;

public class LoginPresenter implements LoginContract.IPresenter {
    LoginContract.IView view;

    public LoginPresenter(LoginContract.IView view) {
        this.view = view;
    }

    @Override
    public void LoginClick(User user) {
        if (user.isValidEmail() && user.isValidPw()){
            view.loginSuccess();
        } else {
            view.loginFailed();
        }
    }
}
