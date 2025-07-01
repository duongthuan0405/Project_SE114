package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;
import android.widget.Toast;

import com.example.tqt_quiz.data.interactor.AnswerSelectInteractorIMP;
import com.example.tqt_quiz.data.interactor.AttemptQuizIMP;
import com.example.tqt_quiz.data.interactor.QuestionrealatedIMP;
import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.data.interactor.SubmitQuizInteractorIMP;
import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.AnswerSelectInteractor;
import com.example.tqt_quiz.domain.interactor.IAttemptQuizInteract;
import com.example.tqt_quiz.domain.interactor.IQuestionrelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.domain.interactor.ISubmitQuizInteractor;
import com.example.tqt_quiz.presentation.contract_vp.DoQuizContract;

import java.util.Collections;
import java.util.List;

public class DoQuizPresenter implements DoQuizContract.IPresenter {
    DoQuizContract.IView view=null;
    IQuestionrelatedInteract questinteracter=null;
    IAttemptQuizInteract Attemptinteracter=null;
    QuizRelatedIMP quizRelatedInteracter=null;
    AnswerSelectInteractor answerSelectInteractor = null;
    ISubmitQuizInteractor submitQuizInteractor = null;
    public DoQuizPresenter (DoQuizContract.IView view)
    {
        this.view=view;
        questinteracter=new QuestionrealatedIMP();
        Attemptinteracter=new AttemptQuizIMP();
        quizRelatedInteracter=new QuizRelatedIMP();
        answerSelectInteractor = new AnswerSelectInteractorIMP();
        submitQuizInteractor = new SubmitQuizInteractorIMP();
    }
    @Override
    public void showQuestion(String quizid) {
        questinteracter.FetchQuizQuestionForStudentWhenDoQuiz(quizid, view.GetTheContext(), new IQuestionrelatedInteract.FetchQuizQuestionForStudentCallBack() {
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
        quizRelatedInteracter.SearchQuizByIdWhenDoQuiz(quizid, view.GetTheContext(), new IQuizRelatedInteract.SearchQuizCallBack() {
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

    @Override
    public void sendAnswer(AttemptQuizDTO currentattemptinfo, String questionId) {
        answerSelectInteractor.SelectAnswer(currentattemptinfo.getId(), questionId, view.GetTheContext(), new AnswerSelectInteractor.SelectAnswerCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailureByExpiredToken() {

            }

            @Override
            public void onFailureByUnAcceptedRole() {

            }

            @Override
            public void onOtherFailure(String msg) {

            }

            @Override
            public void onFailureByCannotSendToServer() {

            }
        });
    }

    @Override
    public void submit(String id) {
        submitQuizInteractor.SubmitQuiz(id, view.GetTheContext(), new ISubmitQuizInteractor.SubmitQuizCallBack() {
            @Override
            public void onSuccess() {
                view.ShowToast("Nộp bài thành công");
                view.Finish();
            }

            @Override
            public void onFailureByExpiredToken() {

            }

            @Override
            public void onFailureByUnAcceptedRole() {

            }

            @Override
            public void onOtherFailure(String msg) {
                view.ShowToast(msg);
                view.Finish();
            }

            @Override
            public void onFailureByCannotSendToServer() {

            }
        });
    }


}
