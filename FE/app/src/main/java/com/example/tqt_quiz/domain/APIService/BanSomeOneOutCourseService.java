package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface BanSomeOneOutCourseService {
    @PATCH("/tqtquiz/JoinCourses/{course_id}/ban/{account_id}")
    Call<Void> BanSomeOneOutCourse(@Path("course_id")String CourseId,@Path("account_id")String AccId);
}
