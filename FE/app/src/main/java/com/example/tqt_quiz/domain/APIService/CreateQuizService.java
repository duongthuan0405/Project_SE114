package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CreateQuizService {
    @POST("/tqtquiz/Quiz/create")
    Call<QuizDTO> CreateQuiz(@Body QuizCreateRequestDTO body);
}
