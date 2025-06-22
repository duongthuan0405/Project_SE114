package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface SubmitQuizService {
    @PATCH("/tqtquiz/AttemptQuizzes/{quiz_id}/submit")
    Call<Void> SubmitQuiz(@Path("quiz_id") String quiz_Id);
}
