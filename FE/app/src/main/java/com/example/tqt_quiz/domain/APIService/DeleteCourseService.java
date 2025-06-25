package com.example.tqt_quiz.domain.APIService;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface DeleteCourseService {
    @DELETE("/tqtquiz/Courses/{course_id}/delete")
    Call<Void> DeleteCourse(@Path("courseid") String courseid);
}
