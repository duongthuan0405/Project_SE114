package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CreateNewCourseService {
    @POST("/tqtquiz/Courses")
    Call<CourseDTO> Create(@Body CourseCreateInfo info);
}
