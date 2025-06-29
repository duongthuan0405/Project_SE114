package com.example.tqt_quiz.presentation.presenter;

import android.content.Intent;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.data.interactor.UpdatePasswordInteractorIMP;
import com.example.tqt_quiz.domain.dto.UpdatePasswordDTO;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.domain.interactor.IAuthInteract;
import com.example.tqt_quiz.domain.interactor.UpdatePasswordInteractor;
import com.example.tqt_quiz.presentation.contract_vp.ChangePasswordContract;

public class ChangePasswordPresenter implements ChangePasswordContract.IPresenter
{
    ChangePasswordContract.IView view;
    UpdatePasswordInteractor updatePasswordInteractor;
    IAccountInteractor accountInteractor;
    public ChangePasswordPresenter(ChangePasswordContract.IView view)
    {
        this.view = view;
        updatePasswordInteractor = new UpdatePasswordInteractorIMP();
        accountInteractor = new AccountInteractorIMP();
    }

    @Override
    public void changePassword(String newPass, String oldPass) {
        updatePasswordInteractor.Updatepassword(new UpdatePasswordDTO(newPass, oldPass), view.getTheContext(), new UpdatePasswordInteractor.UpdatepasswordCallBack() {
            @Override
            public void onSuccess() {
                view.onSuccess();
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void Logout() {
        accountInteractor.logOut(view.getTheContext());
        Intent i = new Intent();
    }
}
