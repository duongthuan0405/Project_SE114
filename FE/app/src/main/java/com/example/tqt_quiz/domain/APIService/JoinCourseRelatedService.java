package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface JoinCourseRelatedService {
    @POST("/tqtquiz/JoinCourses/{course_id}")
    Call<JoinCourseResponseDTO> CreateJoinRequest();
    @PATCH("/tqtquiz/JoinCourses/{account_id}/join/{course_id}/deny")
    Call<Void> DenyJoinRequest();
    @PATCH("/tqtquiz/JoinCourses/{account_id}/join/{course_id}/approve")
    Call<Void> ApproveRequest();
    @PATCH("/tqtquiz/JoinCourses/{course_id}/permissions")
    Call<List<LoginResponse>> ViewAllAccountPendingThisCourse();
}
