package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountWithScore;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

public interface ViewScoreContract 
{
    public interface IView
    {

        Context getTheContext();

        void onSuccessGetQuizInfo(QuizDTO response);

        void navigateToLogin();

        void showMessage(String s);

        void onSuccessShowResult(List<AccountWithScore> response);
    }
    
    public interface IPresenter
    {

        void quizInfo(String quizId);

        void getResultForTeacherView(String quizId);
    }
}
