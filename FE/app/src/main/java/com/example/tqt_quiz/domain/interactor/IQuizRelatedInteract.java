package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

public interface IQuizRelatedInteract {
    void SearchQuizById(String id,Context context,SearchQuizCallBack callBack);
    public interface SearchQuizCallBack
    {
        void onSuccess(QuizDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void FetchALlQuizOfACourse(String Course_ID,Context context,FetchALlQuizOfACourseCallBack callBack);
    public interface FetchALlQuizOfACourseCallBack
    {
        void onSuccess(List<QuizDTO> response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void CreateQuiz(QuizCreateRequestDTO quiz, Context context, CreateQuizCallBack callBack);
    public interface CreateQuizCallBack
    {
        void onSuccess(QuizDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void PublishQuiz(String Quiz_id,Context context,PublishQuizCallBack callBack);
    public interface PublishQuizCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
