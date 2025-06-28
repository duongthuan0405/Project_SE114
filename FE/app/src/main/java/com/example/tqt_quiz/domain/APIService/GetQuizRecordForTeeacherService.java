package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AccountWithScore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetQuizRecordForTeeacherService {
    @GET("/tqtquiz/Quiz/{quiz_id}/all_results")
    Call<List<AccountWithScore>> GetQuizRecordForTeeacher(@Path("quiz_id") String id);
}
