package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;

import java.util.List;

public interface CreateQuizContract {
    public interface IView
    {

        void showOldQuestionOnUI(List<Question> oldQuestions);

        Context getTheContext();

        void CreateQuestionAnswer();

        void navigateToLogin();

        void showMessage(String s);

        void finishAddQuiz();

        void initQuizIdInView(String id);
    }

    public interface IPresenter
    {

        void showOldQuestion(String quizId);

        void createQuiz(QuizCreateRequestDTO quizCreateRequestDTO);

        void addQuestionAnswer(List<CreateQuestionRequest> questionList);
    }
}
