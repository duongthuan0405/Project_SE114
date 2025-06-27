package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;
import android.net.Uri;

import com.example.tqt_quiz.domain.dto.CourseCreateInfo;

public interface CreateCourseContract
{
    public interface IView
    {

        Context getTheContext();

        void showSuccess();

        void finishAddCourse();

        void navigateToLogin();

        void showMessage(String s);
    }

    public interface IPresenter
    {

        void onCreateClick(CourseCreateInfo courseCreateInfo);

    }
}
