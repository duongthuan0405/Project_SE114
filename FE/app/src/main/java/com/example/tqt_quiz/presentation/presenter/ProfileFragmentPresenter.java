package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.presentation.contract_vp.ProfileFragmentContract;

public class ProfileFragmentPresenter implements ProfileFragmentContract.IPresenter
{
    ProfileFragmentContract.IView view;
    IAccountInteractor accountInteractor;
    public ProfileFragmentPresenter(ProfileFragmentContract.IView view)
    {
        this.view = view;
        accountInteractor = new AccountInteractorIMP();
    }

    @Override
    public void onLogoutClick() {
        accountInteractor.logOut(view.getTheContext());
        view.navigationToLogin();
    }

    @Override
    public void getMySelfAccountInfo() {
        accountInteractor.getAccountInfoMySelf(view.getTheContext(), new IAccountInteractor.GetAccountInfoCallBack() {
            @Override
            public void onSuccess(AccountInfo response) {
                view.showInfo(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigationToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Tài khoản không có quyền truy cấp tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotSendToServer(String msg) {
                view.showError(msg);
            }
        });
    }

    @Override
    public void getProfile() {
        accountInteractor.getAccountInfoMySelf(view.getTheContext(), new IAccountInteractor.GetAccountInfoCallBack() {
            @Override
            public void onSuccess(AccountInfo response) {
                view.showInfo(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigationToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotSendToServer(String msg) {
                view.showError("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void showCurrentInfo() {
        accountInteractor.getAccountInfoMySelf(view.getTheContext(), new IAccountInteractor.GetAccountInfoCallBack() {
            @Override
            public void onSuccess(AccountInfo response) {
                view.onGetCurrentInfoSuccess(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigationToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotSendToServer(String msg) {
                view.showError("Không thể kết nối đến server");
            }
        });
    }
}
