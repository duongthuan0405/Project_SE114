package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseCreateInfo;

public interface CreateCourseContract
{
    public interface IView
    {

        Context getTheContext();

        void showSuccess();

        void finishAddCourse();
    }

    public interface IPresenter
    {

        void onCreateClick(CourseCreateInfo courseCreateInfo);
    }
}
