package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;

import java.util.List;

public interface IAttemptQuizInteract {
    void AttemptQuiz(String quizId, Context context,AtttemptQuizCallBack callback);

    void GetAttemptInfo(String quizId, Context context, GetAttemptInfo callback);

    public interface AtttemptQuizCallBack
    {
        void onSuccess(AttemptQuizDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    public interface GetAttemptInfo
    {
        void onSuccess(AttemptQuizDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
        void onNotAttemptYet();
    }
}
