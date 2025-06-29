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
import com.example.tqt_quiz.presentation.contract_vp.DoQuizContract;

import java.util.Collections;
import java.util.List;

public class DoQuizPresenter implements DoQuizContract.IPresenter {
    DoQuizContract.IView view=null;
    QuestionrealatedIMP questinteracter=null;
    AttemptQuizIMP Attemptinteracter=null;
    QuizRelatedIMP quizRelatedInteracter=null;
    public DoQuizPresenter (DoQuizContract.IView view)
    {
        this.view=view;
        questinteracter=new QuestionrealatedIMP();
        Attemptinteracter=new AttemptQuizIMP();
        quizRelatedInteracter=new QuizRelatedIMP();
    }
    @Override
    public void showQuestion(String quizid) {
        questinteracter.FetchQuizQuestionForStudent(quizid, view.GetTheContext(), new IQuestionrelatedInteract.FetchQuizQuestionForStudentCallBack() {
            @Override
            public void onSuccess(List<QuestionDTO> response) {
                view.ShowQuiz(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.NavigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.ShowToast("Ban khong the lam dieu nay");
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
    public void StartAttempt(String quizid) {
        Attemptinteracter.AttemptQuiz(quizid, view.GetTheContext(), new IAttemptQuizInteract.AtttemptQuizCallBack() {
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
                view.ShowToast("Ban khong the lam dieu nay");
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
    public void ShowQuizInfo(String quizid) {
        quizRelatedInteracter.SearchQuizById(quizid, view.GetTheContext(), new IQuizRelatedInteract.SearchQuizCallBack() {
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
                view.ShowToast("Ban khong the lam dieu nay");
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
}
