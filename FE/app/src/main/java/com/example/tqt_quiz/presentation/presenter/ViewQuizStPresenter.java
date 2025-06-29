package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ViewQuizStContract;

public class ViewQuizStPresenter implements ViewQuizStContract.IPresenter
{
    ViewQuizStContract.IView view;
    IQuizRelatedInteract quizRelatedInteract;

    public ViewQuizStPresenter(ViewQuizStContract.IView view)
    {
        this.view = view;
        quizRelatedInteract = new QuizRelatedIMP();
    }

    @Override
    public void getQuizWithScore(String quizId)
    {

        quizRelatedInteract.GetQuizScore(quizId, view.getTheContext(), new IQuizRelatedInteract.GetQuizScoreCallBack() {
            @Override
            public void onSuccess(QuizWithScoreDTO response) {
                view.handleOnResponse(response);
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
