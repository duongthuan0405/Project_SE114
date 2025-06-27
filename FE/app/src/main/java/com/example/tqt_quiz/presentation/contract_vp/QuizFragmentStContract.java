package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public interface QuizFragmentStContract
{
    public interface IView
    {
        Context getTheContext();

        void showOnCourseSpinner(List<Course> response);

        void navigateToLogin();

        void showMessage(String s);

        void showQuiz(List<QuizDTO> quizzes);
    }

    public interface IPresenter
    {

        void loadCourseToSpinner();

        void getAllQuizByFilter(String status);

        void getQuizByFilter(String quizId, String status);
    }
}
