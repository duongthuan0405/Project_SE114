package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.util.List;

public interface ICourseRelatedInteract {
    public void FetchAllCourseJoined(Context context, FetchJoinedCallBack callBack);
    public interface FetchJoinedCallBack
    {
        public void onSuccess(List<CourseDTO> response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistAccount(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer();
    }
    public void FetchAllCourseHosted(Context context,FetchHostedCallBack callBack);
    public interface FetchHostedCallBack
    {
        public void onSuccess(List<CourseDTO> response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistAccount(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer();
    }
    public void FetchAllCoursePending(Context context,FetchAllPendingCallBack callBack);
    public interface FetchAllPendingCallBack
    {
        public void onSuccess(List<CourseDTO> response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistAccount(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer();
    }
    public void FetchAllCourseDenied(Context context,FetchAllDeniedCallBack callBack);
    public interface FetchAllDeniedCallBack
    {
        public void onSuccess(List<CourseDTO> response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistAccount(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer();
    }
    public void FindCourseByID(String course_ID, Context context, FindCourseByIDCallBack callBack);
    public interface FindCourseByIDCallBack
    {
        public void onSuccess(CourseDTO response);
        public void onFailureByExpiredToken(String msg);
        public void onFailureByUnAcepptedRole(String msg);
        public void onFailureByNotExistCourse(String msg);
        public void onFailureByServerError(String msg);
        public void onFailureByCannotSendToServer();
    }
    public void CreateNewCourse(CourseCreateInfo info, Context context, CreateNewCourseCallBack callBack);

    public interface CreateNewCourseCallBack {
        public void onSuccess(CourseDTO response);

        public void onFailureByExpiredToken(String msg);

        public void onFailureByUnAcepptedRole(String msg);

        public void onFailureByNotExistAccount(String msg);

        public void onFailureByServerError(String msg);

        public void onFailureByCannotSendToServer();
    }
    void DeleteCourse(String CourseID,Context context,DeleteCourseCallBack callback);
    public interface DeleteCourseCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
