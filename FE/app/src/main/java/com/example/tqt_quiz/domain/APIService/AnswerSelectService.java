package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AnswerSelectService {
    @PUT("/tqtquiz/Questions_Answers/{attempt_quiz_id}/select/{new_answer_id}")
    Call<Void> AnswerSelect(@Path("attempt_quiz_id") String AttemptQuizId,@Path("new_answer_id") String AnswerId);
}
