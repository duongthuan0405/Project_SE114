package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.QuizDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FetchQuizService {
    @GET("/tqtquiz/Quiz/{quiz_id}")
    Call<QuizDTO> FetchQuizByQuizID();
    @GET("/tqtquiz/Quiz/course/{course_id}")
    Call<List<QuizDTO>> FetchAllQuizByCourseID(@Path("course_id") String Course_id);
    @GET("/tqtquiz/Quiz/all")
    Call<List<QuizDTO>> FetchAllQuiz();
}
