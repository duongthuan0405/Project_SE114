package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface CreateQuestionService {
    @PUT("/tqtquiz/Questions_Answers/create")
    Call<Void> CreateQuestion(@Body List<CreateQuestionRequest> questionRequest);
}
