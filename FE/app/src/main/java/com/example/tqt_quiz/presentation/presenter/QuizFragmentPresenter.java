package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.presentation.contract_vp.QuizFragmentContract;

public class QuizFragmentPresenter implements QuizFragmentContract.IPresenter
{
    QuizFragmentContract.IView view;
    public QuizFragmentPresenter(QuizFragmentContract.IView view)
    {
        this.view = view;
    }

}
