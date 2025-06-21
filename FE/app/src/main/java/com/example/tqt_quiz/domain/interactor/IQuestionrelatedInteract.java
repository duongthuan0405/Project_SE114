package com.example.tqt_quiz.domain.interactor;

import android.content.Context;

import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

public interface IQuestionrelatedInteract {
    void CreateQuestion (CreateQuestionRequest questionRequest, Context context,CreateQuestionCallBack callback);
    public interface CreateQuestionCallBack
    {
        void onSuccess();
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void FetchQuizQuestionForTeacher(String quizId,Context context,FetchQuizQuestionForTeacherCallBack callback);
    public interface FetchQuizQuestionForTeacherCallBack
    {
        void onSuccess(List<QuestionDTO> response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
    void FetchQuizQuestionForStudent(String quizId,Context context,FetchQuizQuestionForStudentCallBack callback);
    public interface FetchQuizQuestionForStudentCallBack
    {
        void onSuccess(List<QuestionDTO> response);
        void onFailureByExpiredToken();
        void onFailureByUnAcceptedRole();
        void onOtherFailure(String msg);
        void onFailureByCannotSendToServer();
    }
}
