package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.CreateQuizContract;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizPresenter implements CreateQuizContract.IPresenter
{
    CreateQuizContract.IView view;
    public CreateQuizPresenter(CreateQuizContract.IView view)
    {
        this.view = view;
    }

    @Override
    public void showOldQuestion(String quizId) {
        List<Question> oldQuestions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("", "", "Dap an 1", true));
        answers.add(new Answer("", "", "Dap an 2", false));
        answers.add(new Answer("", "", "Dap an 3", false));
        answers.add(new Answer("", "", "Dap an 4", false));

        Question q = new Question("", "", "Test thu", answers);
        view.showOldQuestionOnUI(oldQuestions);
    }
}
