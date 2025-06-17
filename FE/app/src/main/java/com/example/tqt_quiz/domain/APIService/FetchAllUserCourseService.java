package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FetchAllUserCourseService {
    @GET("/tqtquiz/Courses/myself/joined")
    Call<List<CourseDTO>> FetchAllCoruseJoined();
    @GET("/tqtquiz/Courses/myself/hosted")
    Call<List<CourseDTO>> FetchAllCourseHosted();
    @GET("/tqtquiz/Courses/myself/pending")
    Call<List<CourseDTO>> FetchAllCoursePending();
    @GET("/tqtquiz/Courses/myself/denied")
    Call<List<CourseDTO>>FetchAllCourseDenied();
}
