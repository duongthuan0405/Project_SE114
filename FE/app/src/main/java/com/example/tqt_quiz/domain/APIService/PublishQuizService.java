package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface PublishQuizService {
    @PATCH("/tqtquiz/Quiz/{quiz_id}/publish")
    Call<Void> QuizPublish(@Path("quiz_id") String id);
}
