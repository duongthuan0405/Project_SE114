package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeleteQuizService {
    @DELETE("/tqtquiz/Quiz/{quiz_id}/delete")
    Call<Void> DeleteQuiz(@Path("quiz_id") String quizid);
}
