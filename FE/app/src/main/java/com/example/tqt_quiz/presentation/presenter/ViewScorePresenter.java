package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.AccountWithScore;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ViewScoreContract;

import java.util.List;

public class ViewScorePresenter implements ViewScoreContract.IPresenter
{
    ViewScoreContract.IView view;
    IQuizRelatedInteract quizRelatedInteract;
    public ViewScorePresenter(ViewScoreContract.IView view)
    {
        this.view = view;
        quizRelatedInteract = new QuizRelatedIMP();
    }

    @Override
    public void quizInfo(String quizId) {
        quizRelatedInteract.SearchQuizById(quizId, view.getTheContext(), new IQuizRelatedInteract.SearchQuizCallBack() {
            @Override
            public void onSuccess(QuizDTO response) {
                view.onSuccessGetQuizInfo(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void getResultForTeacherView(String quizId) {
        quizRelatedInteract.GetQuizRecordForTeeacher(quizId, view.getTheContext(), new IQuizRelatedInteract.GetQuizRecordForTeeacherCallBack() {
            @Override
            public void onSuccess(List<AccountWithScore> response) {
                view.onSuccessShowResult(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }
}
