package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetQuizScoreService {
    @GET("/tqtquiz/Quiz/{quiz_id}/quiz_with_result")
    Call<QuizWithScoreDTO> GetQuizScore(@Path("quiz_id") String id);
}
