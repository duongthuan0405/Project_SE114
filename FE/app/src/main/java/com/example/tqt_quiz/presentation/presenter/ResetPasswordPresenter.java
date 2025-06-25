package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.domain.dto.ResetPasswordRequestDTO;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;
import com.example.tqt_quiz.presentation.contract_vp.ResetPasswordContract;

public class ResetPasswordPresenter implements ResetPasswordContract.IPresenter
{
    private ResetPasswordContract.IView view;
    private IAuthInteract authInteract;

    public ResetPasswordPresenter(ResetPasswordContract.IView view) {
        this.view = view;
        authInteract = new AuthInteractorIMP();
    }

    @Override
    public void resetPassword(String email, String token, String newPassword) {
        ResetPasswordRequestDTO dto = new ResetPasswordRequestDTO(email, token, newPassword);

        authInteract.ResetPassword(dto, view.getTheContext(), new IAuthInteract.ResetPasswordCallBack() {
            @Override
            public void onSuccess() {
                view.showMessage("Mật khẩu đã được cập nhật thành công.");
                view.finishWithOk();
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }
}
