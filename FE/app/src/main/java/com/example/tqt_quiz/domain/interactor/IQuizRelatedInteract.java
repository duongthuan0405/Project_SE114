package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.AccountWithScore;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;

import java.util.List;

public interface IQuizRelatedInteract {
    void SearchQuizById(String id,Context context,SearchQuizCallBack callBack);

    void SearchQuizByIdWhenDoQuiz(String quizid, Context context, SearchQuizCallBack searchQuizCallBack);

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
    void UpdateQuiz(String Quiz_id,QuizCreateRequestDTO updatedquiz,Context context,UpdateQuizCallBack callback);
    public interface UpdateQuizCallBack
    {
        void onSuccess(QuizDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void GetAllQuiz(Context context,GetAllQuizCallBack callback);
    public interface GetAllQuizCallBack
    {
        void onSuccess(List<QuizDTO> response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void DeleteQuiz(String QuizID,Context context,DeleteQuizCallBack callback);
    public interface DeleteQuizCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void GetQuizScore(String QUizId,Context context,GetQuizScoreCallBack callback);
    public interface GetQuizScoreCallBack
    {
        void onSuccess(QuizWithScoreDTO response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void GetQuizRecordForTeeacher(String quizid,Context context,GetQuizRecordForTeeacherCallBack callback);
    public interface GetQuizRecordForTeeacherCallBack
    {
        void onSuccess(List<AccountWithScore> response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
