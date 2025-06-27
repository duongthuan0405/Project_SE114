package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface LeaveCourseService {
    @PATCH("/tqtquiz/JoinCourses/{course_id}/leave")
    Call<Void> LeaveCourse(@Path("course_id") String CourseId);
}
