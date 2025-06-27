package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UpdateQuizService {
    @PUT("/tqtquiz/Quiz/{quiz_id}/update")
    Call<QuizDTO> UpdateQuiz(@Path("quiz_id") String id,@Body QuizCreateRequestDTO updatedquiz);
}
