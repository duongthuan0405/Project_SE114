package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.AccountInteractorIMP;
import com.example.tqt_quiz.data.repository.token.TokenManager;
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
}
