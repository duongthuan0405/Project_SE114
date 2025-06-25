package com.example.tqt_quiz.domain.interactor;

import android.content.Context;
import android.net.Uri;

import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.UploadResponse;

import java.util.List;

public interface IImageRelatedInteract
{
    public void uploadAvatar(Uri imageUri, Context context, UploadImageCallBack callBack);
    public void uploadLogo(Uri imageUri, String courseId, Context context, UploadImageCallBack callBack);
    public interface UploadImageCallBack
    {
        public void onSuccess(UploadResponse response);
        public void onFailureByExpiredToken();
        public void onFailureByUnAcepptedRole();
        public void onFailureByOther(String msg);
        public void onFailureByCannotConnectToServer();
    }
}
