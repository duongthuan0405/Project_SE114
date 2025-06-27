package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.util.List;

public interface ICourseFragmentStContract {

    public interface IView {
        Context getTheContext();

        void showCourseList(List<CourseDTO> courseList);

        void navigateToLogin();

        void showError(String message);
    }

    public interface IPresenter {
        void loadJoinedCourses();

        void loadPendingCourses();
    }
}
