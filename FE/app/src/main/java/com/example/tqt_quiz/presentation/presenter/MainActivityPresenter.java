package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.interactor.IAccountInteractor;
import com.example.tqt_quiz.presentation.contract_vp.IMainActivityContract;
import com.example.tqt_quiz.staticclass.StaticClass;

public class MainActivityPresenter implements IMainActivityContract.IPresenter
{
    public IMainActivityContract.IView mainView;
    public IAccountInteractor accountInteractor;

    public MainActivityPresenter(IMainActivityContract.IView view)
    {
        mainView = view;
        accountInteractor = new AccountInteractorIMP();
    }

    @Override
    public void onDecideToNavigate() {

        accountInteractor.getAccountInfoMySelf(mainView.getTheContext(), new IAccountInteractor.GetAccountInfoCallBack() {
            @Override
            public void onSuccess(AccountInfo response) {

                if(response.getAccountTypeId().equals(StaticClass.AccountTypeId.teacher))
                    mainView.navigateToMainHomeForTeacher();
                else if (response.getAccountTypeId().equals(StaticClass.AccountTypeId.student))
                    mainView.navigateToMainHomeForStudent();
                else if (response.getAccountTypeId().equals(StaticClass.AccountTypeId.admin))
                    mainView.navigateToMainHomeForAdmin();
                else
                    mainView.showToast("Lỗi phân quyền");

            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                mainView.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                mainView.showToast(msg);
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                mainView.showToast(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                mainView.showToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer(String msg) {
                mainView.showToast(msg);
            }
        });
    }
}
