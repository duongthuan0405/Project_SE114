package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public interface ViewCourseStContract
{
    public interface IView
    {
        Context GetTheContext();
        void ShowCourse(CourseDTO courseDTO);
        void ShowAllMemBerInCourse(List<AccountInfo> MemberList);
        void ShowToast(String msg);
    }

    public interface IPresenter
    {

        void showCourseInfo(String courseId);

        void showListMemberOfCourse(String courseId);
        void LeaveCourse(String courseId);
    }
}
