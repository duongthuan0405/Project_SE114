package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseDTO;

public interface JoinCourseSTContract {
    public interface IView
    {
        Context GetContext();
        void ShowSearchedCourse(CourseDTO Course);
        void JoinCourse(String Id);
        void BindingUI();
        void ShowToast(String msg);
        void DisableJoin();
        void EnableJoin();
        void navigateToLogin();
    }

    public interface IPresentor
    {
        void SearchCourse(String Id);
        void JoinCourse(String Id);
    }
}
