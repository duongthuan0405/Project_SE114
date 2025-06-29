package com.example.tqt_quiz.presentation.presenter;

import android.net.Uri;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.data.interactor.ImageRelatedInteract;
import com.example.tqt_quiz.data.interactor.UpdateAccountInteractorIMP;
import com.example.tqt_quiz.domain.dto.UpdateAccountInfoDTO;
import com.example.tqt_quiz.domain.dto.UploadResponse;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.domain.interactor.IAccountUpdateInteractor;
import com.example.tqt_quiz.domain.interactor.IImageRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ChangeProfileContract;

public class ChangeProfilePresenter implements ChangeProfileContract.IPresenter
{
    ChangeProfileContract.IView view;
    IImageRelatedInteract imageRelatedInteract;
    IAccountUpdateInteractor accountUpdateInteractorInteractor;
    public ChangeProfilePresenter(ChangeProfileContract.IView view)
    {
        this.view = view;
        imageRelatedInteract = new ImageRelatedInteract();
        accountUpdateInteractorInteractor = new UpdateAccountInteractorIMP();
    }

    @Override
    public void saveAvatar(Uri selectedImageUri) {
        imageRelatedInteract.uploadAvatar(selectedImageUri, view.getTheContext(), new IImageRelatedInteract.UploadImageCallBack() {
            @Override
            public void onSuccess(UploadResponse response) {
                view.onResponse(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByOther(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void onChangeProfile(String firstname, String lastname) {
        UpdateAccountInfoDTO a = new UpdateAccountInfoDTO(lastname, firstname);
        accountUpdateInteractorInteractor.UpdateAccountInfo(a, view.getTheContext(), new IAccountUpdateInteractor.UpdateAccountInfoCallBack() {
            @Override
            public void onSuccess() {
                view.finishOK();
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập đến tài nguyên này");
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
}
