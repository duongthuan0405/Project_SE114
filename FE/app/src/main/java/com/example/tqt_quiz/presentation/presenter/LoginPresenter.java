package com.example.tqt_quiz.presentation.presenter;

import android.accounts.Account;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;

public class LoginPresenter implements LoginContract.IPresenter {
    LoginContract.IView view;
    IAuthInteract authInteract;

    public LoginPresenter(LoginContract.IView view) {
        this.view = view;
        authInteract = new AuthInteractorIMP();
    }

    @Override
    public void LoginClick(String email, String password) {
        Log.d("KKK", email + " - " + password);
        if (email.equals("") || password.equals("")) {
            view.showLoginError("Vui lòng điền đầy đủ thông tin");
            return;
        }

        authInteract.Login(email, password, view.getContext(), new IAuthInteract.LoginCallBack() {
            @Override
            public void onSuccess(LoginResponse response) {
                view.loginSuccess(response.getRoleId());
            }

            @Override
            public void onUnAuthorized(String msg) {
                view.showLoginError(msg);
            }

            @Override
            public void FailedByNotResponse(String msg) {
                view.showLoginError(msg);
            }
        });

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
