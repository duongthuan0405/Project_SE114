package com.example.tqt_quiz.presentation.presenter;

import android.text.TextUtils;

import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.domain.dto.RegisterRequest;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;
import com.example.tqt_quiz.presentation.contract_vp.RegisterContract;

public class RegisterPresenter implements RegisterContract.IPresenter {
    RegisterContract.IView view;
    IAuthInteract authInteract;
    public RegisterPresenter(RegisterContract.IView view) {
        this.view = view;
        authInteract = new AuthInteractorIMP();
    }

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

        authInteract.Register(new RegisterRequest(email, password, firstName, lastName), view.getTheContext(), new IAuthInteract.RegCallBack() {
            @Override
            public void onSuccess() {
                view.showRegisterSuccess();
            }

            @Override
            public void onFailedRegister(String msg) {
                view.showRegisterError(msg);
            }

            @Override
            public void FailedByNotResponse(String msg) {
                view.showRegisterError(msg);
            }
        });

    }
}
