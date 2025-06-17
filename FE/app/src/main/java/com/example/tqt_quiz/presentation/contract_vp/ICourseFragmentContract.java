package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.util.List;

public interface ICourseFragmentContract {
    public interface IView
    {

        Context getTheContext();

        void showAllList(List<CourseDTO> response);

        void navigateToLogin();

        void showError(String s);
    }

    public interface IPresenter
    {

        void showAllMyCourse();
    }
}
