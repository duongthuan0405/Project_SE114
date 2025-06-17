package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountInfo;

public interface IAccountInteractor
{
    public void getAccountInfoById(String account_id, Context context, GetAccountInfoCallBack callBack);
    public void getAccountInfoMySelf(Context context, GetAccountInfoCallBack callBack);

    void logOut(Context context);

    interface GetAccountInfoCallBack
    {
        public void onSuccess(AccountInfo response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistAccount(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer(String msg);
    }
}
