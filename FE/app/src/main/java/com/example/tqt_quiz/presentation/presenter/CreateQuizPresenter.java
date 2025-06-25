package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.QuestionrealatedIMP;
import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuestionrelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.adapters.CourseAdapterForSpinner;
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.CreateQuizContract;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateQuizPresenter implements CreateQuizContract.IPresenter
{
    CreateQuizContract.IView view;
    IQuizRelatedInteract quizRelatedInteract;
    IQuestionrelatedInteract questionrelatedInteract;
    ICourseRelatedInteract courseRelatedInteract;
    public CreateQuizPresenter(CreateQuizContract.IView view)
    {
        this.view = view;
        quizRelatedInteract = new QuizRelatedIMP();
        questionrelatedInteract = new QuestionrealatedIMP();
        courseRelatedInteract = new CourseRelatedInteractIMP();

    }

    @Override
    public void onDetailOldQuiz(String quizId) {
            quizRelatedInteract.SearchQuizById(quizId, view.getTheContext(), new IQuizRelatedInteract.SearchQuizCallBack() {
            @Override
            public void onSuccess(QuizDTO response) {
                view.showQuizInfo(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không có quyền truy cập tài nguyên này");
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

    @Override
    public void onGetOldQuestion(String id) {
        questionrelatedInteract.FetchQuizQuestionForTeacher(id, view.getTheContext(), new IQuestionrelatedInteract.FetchQuizQuestionForTeacherCallBack() {
            @Override
            public void onSuccess(List<QuestionDTO> response) {
                view.showQuestion(response);
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
    public void updateQuiz(String quizId, QuizCreateRequestDTO quizCreateRequestDTO) {
        quizRelatedInteract.UpdateQuiz(quizId, quizCreateRequestDTO, view.getTheContext(), new IQuizRelatedInteract.UpdateQuizCallBack() {
            @Override
            public void onSuccess(QuizDTO response) {
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
    public void loadCourseList() {
        courseRelatedInteract.FetchAllCourseHosted(view.getTheContext(), new ICourseRelatedInteract.FetchHostedCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.loadOnSpinner(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");;
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }
}
