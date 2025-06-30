package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AttemptQuizService {
    @POST("/tqtquiz/AttemptQuizzes/{quiz_id}/attempt")
    Call<AttemptQuizDTO> AttemptQuiz(@Path("quiz_id") String id);

    @GET("/tqtquiz/AttemptQuizzes/{quiz_id}/attempt_info")
    Call<AttemptQuizDTO> GetAttemptInfo(@Path("quiz_id") String id);
}
