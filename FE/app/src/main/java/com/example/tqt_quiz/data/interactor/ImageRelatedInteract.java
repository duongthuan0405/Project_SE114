package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.ImageService;
import com.example.tqt_quiz.domain.APIService.JoinCourseRelatedService;
import com.example.tqt_quiz.domain.dto.UploadResponse;
import com.example.tqt_quiz.domain.imagehelper.ImageHelper;
import com.example.tqt_quiz.domain.interactor.IImageRelatedInteract;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRelatedInteract implements IImageRelatedInteract
{
    @Override
    public void uploadAvatar(Uri imageUri, Context context, UploadImageCallBack callBack) {
        try
        {
            MultipartBody.Part part = ImageHelper.uriToPart(context, imageUri, "file");

            TokenManager tokenManager=new TokenManager(context);
            ImageService service = RetrofitClient.GetClient(tokenManager).create(ImageService.class);

            Call<UploadResponse> call = service.uploadAvatar(part);

            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if(response.isSuccessful())
                    {
                        callBack.onSuccess(response.body());
                    }
                    else
                    {
                        String rawJson="";
                        try
                        {
                            rawJson = response.errorBody().string();
                            JSONObject obj = new JSONObject(rawJson);
                            String msg = obj.optString("message");
                            callBack.onFailureByOther(msg);
                        } catch (Exception e) {
                            if(response.code()==401)
                            {
                                callBack.onFailureByExpiredToken();
                            }
                            else if(response.code()==403)
                            {
                                callBack.onFailureByUnAcepptedRole();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    callBack.onFailureByCannotConnectToServer();
                }
            });
        }
        catch (Exception e)
        {
            callBack.onFailureByOther("Lỗi");
        }

    }

    @Override
    public void uploadLogo(Uri imageUri, String courseId, Context context, UploadImageCallBack callBack) {
        try
        {
            Log.d("THUAN", imageUri == null ? "null" : imageUri.toString());
            MultipartBody.Part part = ImageHelper.uriToPart(context, imageUri, "file");

            TokenManager tokenManager=new TokenManager(context);
            ImageService service = RetrofitClient.GetClient(tokenManager).create(ImageService.class);


            Call<UploadResponse> call = service.uploadCourseLogo(courseId, part);

            call.enqueue(new Callback<UploadResponse>() {
                @Override
                public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                    if(response.isSuccessful())
                    {
                        callBack.onSuccess(response.body());
                    }
                    else
                    {
                        String rawJson="";
                        try
                        {
                            rawJson = response.errorBody().string();
                            JSONObject obj = new JSONObject(rawJson);
                            String msg = obj.optString("message");
                            callBack.onFailureByOther(msg);
                        } catch (Exception e) {
                            if(response.code()==401)
                            {
                                callBack.onFailureByExpiredToken();
                            }
                            else if(response.code()==403)
                            {
                                callBack.onFailureByUnAcepptedRole();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UploadResponse> call, Throwable t) {
                    callBack.onFailureByCannotConnectToServer();
                }
            });
        }
        catch (Exception e)
        {
            callBack.onFailureByOther("Lỗi");
        }
    }
}
