package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;
import android.net.Uri;

import com.example.tqt_quiz.domain.dto.UploadResponse;

public interface ChangeProfileContract
{
    public interface IView
    {

        Context getTheContext();

        void onResponse(UploadResponse response);

        void navigateToLogin();

        void showMessage(String s);

        void finishOK();
    }

    public interface IPresenter
    {

        void saveAvatar(Uri selectedImageUri);

        void onChangeProfile(String string, String string1);
    }
}
