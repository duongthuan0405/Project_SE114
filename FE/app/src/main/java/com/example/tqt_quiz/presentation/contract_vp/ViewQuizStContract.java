package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;

public interface ViewQuizStContract
{
    public interface IView
    {

        Context getTheContext();

        void handleOnResponse(QuizWithScoreDTO response);

        void navigateToLogin();

        void showMessage(String s);
    }
    
    public interface IPresenter
    {

        void getQuizWithScore(String quizId);
    }
}
