package com.example.tqt_quiz.presentation.contract_vp;

import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;

import java.util.List;

public interface CreateQuizContract {
    public interface IView
    {

        void showOldQuestionOnUI(List<Question> oldQuestions);
    }

    public interface IPresenter
    {

        void showOldQuestion(String quizId);
    }
}
