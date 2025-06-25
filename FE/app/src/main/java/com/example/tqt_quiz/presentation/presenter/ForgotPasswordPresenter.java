package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.domain.dto.ForgotPasswordRequestDTO;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;
import com.example.tqt_quiz.presentation.contract_vp.ForgotPasswordContract;

public class ForgotPasswordPresenter implements ForgotPasswordContract.IPresenter
{
    private ForgotPasswordContract.IView view;
    private IAuthInteract authInteract;
    public ForgotPasswordPresenter(ForgotPasswordContract.IView view)
    {
        this.view = view;
        authInteract = new AuthInteractorIMP();
    }

    @Override
    public void onClickContinue(String email) {
        authInteract.ForgotPassword(new ForgotPasswordRequestDTO(email), view.getTheContext(), new IAuthInteract.ForgotPasswordCallBack() {
            @Override
            public void onSuccess() {
                view.showMessage("Đã gửi mã xác thực đặt lại mật khẩu đến mail của bạn");
                view.navigateToResetPassword();
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer(String msg) {
                view.showMessage(msg);
            }
        });
    }
}
