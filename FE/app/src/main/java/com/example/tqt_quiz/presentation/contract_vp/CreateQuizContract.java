package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.classes.Question;

import java.util.List;

public interface CreateQuizContract {
    public interface IView
    {

       Context getTheContext();

        void CreateQuestionAnswer();

        void navigateToLogin();

        void showMessage(String s);

        void finishAddQuiz();

        void initQuizIdInView(String id);

        void showQuizInfo(QuizDTO response);

        void showQuestion(List<QuestionDTO> response);

        void loadOnSpinner(List<CourseDTO> response);

        void Finish();
    }

    public interface IPresenter
    {

        void onDetailOldQuiz(String quizId);

        void createQuiz(QuizCreateRequestDTO quizCreateRequestDTO);

        void addQuestionAnswer(List<CreateQuestionRequest> questionList);

        void onGetOldQuestion(String id);

        void updateQuiz(String quizId, QuizCreateRequestDTO quizCreateRequestDTO);

        void loadCourseList();
    }
}
