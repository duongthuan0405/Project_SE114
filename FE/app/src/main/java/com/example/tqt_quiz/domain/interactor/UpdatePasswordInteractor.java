package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.UpdatePasswordDTO;

import retrofit2.http.Body;
import retrofit2.http.Path;

public interface UpdatePasswordInteractor {
    void Updatepassword(UpdatePasswordDTO info, Context context,UpdatepasswordCallBack callback);
    public interface UpdatepasswordCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
