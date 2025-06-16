package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.data.interactor.MainInteractorImp;
import com.example.tqt_quiz.presentation.contract_vp.MainActitvityContract;

public class MainActivityPresenter implements MainActitvityContract.IPresenter
{
    MainActitvityContract.IView view = null;
    MainInteractorImp interactor = null;
    AuthInteractorIMP Authinteractor=null;
    public MainActivityPresenter(MainActitvityContract.IView view)
    {
        this.view = view;
        interactor = new MainInteractorImp();
    }
    @Override
    public void onCreateActivity()
    {
        String data = interactor.getDataToShowView();
        view.showToast(data);

    }
}
