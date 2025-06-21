package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.presentation.contract_vp.MemberInfoContract;
import com.example.tqt_quiz.presentation.view.activities.MemberInfo;

public class MemberInfoPresenter implements MemberInfoContract.IPresenter
{
    MemberInfoContract.IView view;
    IAccountInteractor accountInteractor;
    public MemberInfoPresenter(MemberInfoContract.IView view)
    {
        this.view = view;
        accountInteractor = new AccountInteractorIMP();
    }

    @Override
    public void showInfo(String memberId) {
        accountInteractor.getAccountInfoById(memberId, view.getTheContext(), new IAccountInteractor.GetAccountInfoCallBack() {
            @Override
            public void onSuccess(AccountInfo response) {

                view.showInfo(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showToast("Tài khoản không có quyền truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer(String msg) {
                view.showToast(msg);
            }
        });
    }
}
