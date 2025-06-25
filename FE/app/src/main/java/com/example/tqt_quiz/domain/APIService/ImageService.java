package com.example.tqt_quiz.domain.APIService;

import com.example.tqt_quiz.domain.dto.UploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageService
{
    @Multipart
    @POST("tqtquiz/image/avatar/account")
    Call<UploadResponse> uploadAvatar(@Part MultipartBody.Part file);

    @Multipart
    @POST("tqtquiz/image/logo/course/{courseId}")
    Call<UploadResponse> uploadCourseLogo(@Path("courseId") String courseId, @Part MultipartBody.Part file);
}
