package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.AttemptQuizIMP;
import com.example.tqt_quiz.data.interactor.QuestionrealatedIMP;
import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.IAttemptQuizInteract;
import com.example.tqt_quiz.domain.interactor.IQuestionrelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ViewResultContract;

import java.util.List;

public class ViewResultPresenter implements ViewResultContract.IPresenter {

    ViewResultContract.IView view=null;
    AttemptQuizIMP AttemptQuizInteractor=null;
    QuestionrealatedIMP QuestionInteractor=null;
    QuizRelatedIMP QuizInteractor=null;
    public ViewResultPresenter(ViewResultContract.IView view)
    {
        this.view=view;
        AttemptQuizInteractor=new AttemptQuizIMP();
        QuestionInteractor=new QuestionrealatedIMP();
        QuizInteractor=new QuizRelatedIMP();
    }
    @Override
    public void ShowQuizResult(String quizid) {
        QuestionInteractor.FetchQuizQuestionForStudent(quizid, view.GetTheContext(), new IQuestionrelatedInteract.FetchQuizQuestionForStudentCallBack() {
            @Override
            public void onSuccess(List<QuestionDTO> response) {
                view.ShowQuizResult(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.NavigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.ShowToast("Ban khong co quyen thuc hien dieu nay");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Mat ket noi voi may chu");
            }
        });
    }

    @Override
    public void GetAttempt(String quizid) {
        AttemptQuizInteractor.GetAttemptInfo(quizid, view.GetTheContext(), new IAttemptQuizInteract.GetAttemptInfo() {
            @Override
            public void onSuccess(AttemptQuizDTO response) {
                view.SaveAttemptInfo(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.NavigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.ShowToast("Ban khong co quyen lam dieu nay");
            }

            @Override
            public void onOtherFailure(String msg) {

                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Mat ket noi voi may chu");
            }

            @Override
            public void onNotAttemptYet() {
                view.SaveAttemptInfo(null);
            }
        });
    }

    @Override
    public void ShowQuizInfo(String quizid) {
        QuizInteractor.SearchQuizById(quizid, view.GetTheContext(), new IQuizRelatedInteract.SearchQuizCallBack() {
            @Override
            public void onSuccess(QuizDTO response) {
                view.ShowQuizInfo(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.NavigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.ShowToast("Ban khong co quyen lam dieu nay");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Khong the ket noi voi may chu");
            }
        });
    }

}
