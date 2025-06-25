package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;

import java.util.List;

public interface IJoinCourseInteract {
    public void JoinCourse(String course_id, Context context, JoinCourseCallBack callBack);
    public interface JoinCourseCallBack
    {
        public void onSuccess(JoinCourseResponseDTO response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcceptedRole(String msg);
        public void onFailureByNotExistCourse(String msg);
        public void onFailureByServer(String msg);
        public void onFailureByCannotSendToServer();
    }


    public void AcceptCourseJoinRequest(String account_id, String course_id, Context context, AcceptCourseCallBack callBack);
    public interface AcceptCourseCallBack
    {
        public void onSuccess(String msg);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onOtherFailure(String msg);
        public void onCannotConnectToServer(String msg);
    }

    public void DenyCourseJoinRequest(String account_id, String course_id, Context context, DenyCourseCallBack callBack);
    public interface DenyCourseCallBack
    {
        public void onSuccess(String msg);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onOtherFailure(String msg);
        public void onCannotConnectToServer(String msg);
    }


    public void ViewAllAccountPendingThisCourse(String course_id, Context context, ViewAllAccountPendingThisCourse callBack);
    public interface ViewAllAccountPendingThisCourse
    {
        public void onSuccess(List<AccountInfo> response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnacceptedRole(String msg);
        public void onFailureByNotExistCourse(String msg);
        public void onFailureByServerError(String msg);
        public void onCannotContactWithServer();
    }
}
