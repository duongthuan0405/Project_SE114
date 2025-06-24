package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.QuestionDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FetchQuestionService {
    @GET("/tqtquiz/Questions_Answers/{quizId}/get_for_teacher")
    Call<List<QuestionDTO>> FetchQuizQuestionForTeacher(@Path("quizId") String quizId);
    @GET("/tqtquiz/Questions_Answers/{quizId}/get_for_student")
    Call<List<QuestionDTO>> FetchQuizQuestionForStudent(@Path("quizId")String quizId);
}
