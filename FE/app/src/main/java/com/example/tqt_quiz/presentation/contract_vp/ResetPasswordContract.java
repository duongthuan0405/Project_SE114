package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.tqt_quiz.presentation.view.activities.ResetPassword;

public interface ResetPasswordContract
{
    public interface IView
    {
        public Context getTheContext();

        public void showMessage(String msg);

        public void finishWithOk();
    }

    public interface IPresenter
    {

        void resetPassword(String email, String token, String newPassword);
    }
}
