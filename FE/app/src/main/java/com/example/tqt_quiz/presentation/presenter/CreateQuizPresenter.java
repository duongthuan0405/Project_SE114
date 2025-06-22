package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.tqt_quiz.data.interactor.QuestionrealatedIMP;
import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.IQuestionrelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.CreateQuizContract;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizPresenter implements CreateQuizContract.IPresenter
{
    CreateQuizContract.IView view;
    IQuizRelatedInteract quizRelatedInteract;
    IQuestionrelatedInteract questionrelatedInteract;
    public CreateQuizPresenter(CreateQuizContract.IView view)
    {
        this.view = view;
        quizRelatedInteract = new QuizRelatedIMP();
        questionrelatedInteract = new QuestionrealatedIMP();

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

    @Override
    public void createQuiz(QuizCreateRequestDTO quizCreateRequestDTO) {

        quizRelatedInteract.CreateQuiz(quizCreateRequestDTO, view.getTheContext(), new IQuizRelatedInteract.CreateQuizCallBack() {
            @Override
            public void onSuccess(QuizDTO response) {
                view.initQuizIdInView(response.getId());
                view.CreateQuestionAnswer();
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void addQuestionAnswer(List<CreateQuestionRequest> questionList) {

        questionrelatedInteract.CreateQuestion(questionList, view.getTheContext(), new IQuestionrelatedInteract.CreateQuestionCallBack() {
            @Override
            public void onSuccess() {
                view.showMessage("Thêm câu hỏi thành công");
                view.finishAddQuiz();
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }
}
