package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

public interface DoQuizContract {
    public interface IView
    {
        Context GetTheContext();
        void ShowQuiz(List<QuestionDTO> questionlist);
        void NavigateToLogin();
        void ShowToast(String msg);
        void SaveAttemptInfo(AttemptQuizDTO info);
        void ShowQuizInfo(QuizDTO info);

        void Finish();

        void showAlertDialogToNotify();
    }
    public interface IPresenter
    {
      void showQuestion(String quizid);
      void StartAttempt(String quizid);
      void ShowQuizInfo(String quizid);
      void sendAnswer(AttemptQuizDTO currentattemptinfo, String questionId);

        void submit(String id);

    }

}
