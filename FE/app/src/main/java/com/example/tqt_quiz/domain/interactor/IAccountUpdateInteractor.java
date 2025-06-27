package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.UpdateAccountInfoDTO;

public interface IAccountUpdateInteractor {
    void UpdateAccountInfo(UpdateAccountInfoDTO info, Context context,UpdateAccountInfoCallBack callback);
    public interface UpdateAccountInfoCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
