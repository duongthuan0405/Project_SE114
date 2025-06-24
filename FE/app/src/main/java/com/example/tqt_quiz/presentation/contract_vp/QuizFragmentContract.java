package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public interface QuizFragmentContract
{
    public interface IView
    {

        Context getTheContext();

        void navigateToLogin();

        void showMessage(String s);

        void showOnSpinnerCourse(List<Course> courses);
        void showQuiz(List<QuizDTO> quizzes);
    }

    public interface IPresenter
    {

        void loadCourseToSpinner();

        void getQuizByFilter(String selectedCourse, String selectedStatus);
    }
}
