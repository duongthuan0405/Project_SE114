package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

public interface AnswerSelectInteractor {
    void SelectAnswer(String AttemptQuizId, String AnswerId, Context context,SelectAnswerCallBack callback);
    public interface SelectAnswerCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
