package com.example.tqt_quiz.presentation.presenter;

import android.text.TextUtils;

import com.example.tqt_quiz.presentation.contract_vp.RegisterContract;

public class RegisterPresenter implements RegisterContract.IPresenter {
    RegisterContract.IView view;
    public RegisterPresenter(RegisterContract.IView view) { this.view = view; }

    @Override
    public void handleRegister(String type, String lastName, String firstName, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(lastName) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            view.showRegisterError("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.showRegisterError("Mật khẩu không khớp.");
            return;
        }

        view.showRegisterSuccess();
    }
}
