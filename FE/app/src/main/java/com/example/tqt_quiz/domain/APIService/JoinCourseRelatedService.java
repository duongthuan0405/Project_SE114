package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JoinCourseRelatedService {
    @POST("/tqtquiz/JoinCourses/{course_id}")
    Call<JoinCourseResponseDTO> CreateJoinRequest(@Path("course_id") String course_id);
    @PATCH("/tqtquiz/JoinCourses/{account_id}/join/{course_id}/deny")
    Call<Void> DenyJoinRequest(@Path("account_id") String account_id, @Path("course_id") String course_id);
    @PATCH("/tqtquiz/JoinCourses/{account_id}/join/{course_id}/approve")
    Call<Void> ApproveRequest(@Path("account_id") String account_id, @Path("course_id") String course_id);
    @GET("/tqtquiz/JoinCourses/{course_id}/permissions")
    Call<List<AccountInfo>> ViewAllAccountPendingThisCourse(@Path("course_id") String course_id);
}
