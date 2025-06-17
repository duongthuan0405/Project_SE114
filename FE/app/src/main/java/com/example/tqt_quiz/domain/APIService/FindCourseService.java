package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.CourseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FindCourseService {
    @GET("/tqtquiz/Courses/{course_id}")
    Call<CourseDTO> FindCourseByID(@Path("course_id") String course_id);
}
