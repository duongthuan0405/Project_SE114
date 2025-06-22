package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

public interface ISubmitQuizInteractor {
    void SubmitQuiz(String quizId, Context context,SubmitQuizCallBack callback);
    public interface SubmitQuizCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
