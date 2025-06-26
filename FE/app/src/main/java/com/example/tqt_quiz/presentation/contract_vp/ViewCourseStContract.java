package com.example.tqt_quiz.presentation.contract_vp;

public interface ViewCourseStContract
{
    public interface IView
    {

    }

    public interface IPresenter
    {

        void showCourseInfo(String courseId);

        void showListMemberOfCourse(String courseId);
    }
}
