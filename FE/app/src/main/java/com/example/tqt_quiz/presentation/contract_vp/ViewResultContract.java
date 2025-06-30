package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

public interface ViewResultContract {
    public interface IView
    {
        Context GetTheContext();
        void NavigateToLogin();
        void ShowQuizResult(List<QuestionDTO> questionlist);
        void SaveAttemptInfo(AttemptQuizDTO info);
        void ShowToast(String msg);
        void ShowQuizInfo(QuizDTO info);
    }
    public interface IPresenter
    {
        void ShowQuizResult(String quizid);
        void GetAttempt(String quizid);
        void ShowQuizInfo(String quizid);
    }
}
