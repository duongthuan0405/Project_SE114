package com.example.tqt_quiz.presentation.contract_vp;

public interface MainActitvityContract
{
    interface IView
    {
        void showToast(String data);
    }

    interface IPresenter
    {
        void onCreateActivity();
    }

}
