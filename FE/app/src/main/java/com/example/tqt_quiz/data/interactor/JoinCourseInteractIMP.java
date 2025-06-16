package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;


import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.JoinCourseRelatedService;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;
import com.example.tqt_quiz.domain.interactor.IJoinCourseInteract;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinCourseInteractIMP implements IJoinCourseInteract {
    @Override
    public void JoinCourse(Context context, JoinCourseCallBack callBack) {
        TokenManager tokenManager = new TokenManager(context);
        JoinCourseRelatedService service = RetrofitClient.GetClient(tokenManager).create(JoinCourseRelatedService.class);
        Call<JoinCourseResponseDTO> call = service.CreateJoinRequest();
        call.enqueue(new Callback<JoinCourseResponseDTO>() {
            @Override
            public void onResponse(Call<JoinCourseResponseDTO> call, Response<JoinCourseResponseDTO> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    String rawJson = "";
                    try {
                        int code = response.code();
                        rawJson = response.errorBody().string();
                        JSONObject obj = new JSONObject(rawJson);
                        String msg = response.errorBody().string();
                        switch (code) {
                            case 404:
                                callBack.onFailureByNotExistCourse(msg);
                                break;
                            case 500:
                                callBack.onFailureByServer(msg);
                                break;
                        }
                    } catch (Exception e) {
                        if(response.code() == 401)
                        {
                            callBack.onFailureByExpiredToken("");
                        }
                        else if(response.code() == 403)
                        {
                            callBack.onFailureByUnAcceptedRole("");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JoinCourseResponseDTO> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void AcceptCourseJoinRequest(Context context, AcceptCourseCallBack callBack)
    {
        TokenManager tokenManager=new TokenManager(context);
        JoinCourseRelatedService service=RetrofitClient.GetClient(tokenManager).create(JoinCourseRelatedService.class);
        Call<Void> call= service.ApproveRequest();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess("Đã chấp nhận yêu cầu");
                }
                else
                {
                    String rawJson="";
                    try
                    {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callBack.onOtherFailure(msg);

                    } catch (Exception e) {
                        if(response.code() == 401)
                        {
                            callBack.onFailureByExpiredToken("");
                        }
                        else if(response.code() == 403)
                        {
                            callBack.onFailureByUnAcepptedRole("");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                    callBack.onCannotConnectToServer("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void DenyCourseJoinRequest(Context context, DenyCourseCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        JoinCourseRelatedService service=RetrofitClient.GetClient(tokenManager).create(JoinCourseRelatedService.class);
        Call<Void> call= service.DenyJoinRequest();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess("Đã từ chối yêu cầu tham gia");
                }
                else
                {
                    String rawJson="";
                    try
                    {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        if(response.code() == 500)
                        {
                            callBack.onCannotConnectToServer(msg);
                        }
                        else
                        {
                            callBack.onOtherFailure(msg);
                        }
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                        {
                            callBack.onFailureByExpiredToken("");
                        }
                        else if(response.code() == 403)
                        {
                            callBack.onFailureByUnAcepptedRole("");
                        }
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onCannotConnectToServer("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void ViewAllAccountPendingThisCourse(Context context, ViewAllAccountPendingThisCourse callBack) {
        TokenManager tokenManager = new TokenManager(context);
        JoinCourseRelatedService service = RetrofitClient.GetClient(tokenManager).create(JoinCourseRelatedService.class);
        Call<List<LoginResponse>> call = service.ViewAllAccountPendingThisCourse();
        call.enqueue(new Callback<List<LoginResponse>>() {


            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    String rawJson="";
                    try
                    {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.getString("message");
                        switch (code)
                        {
                            case 404:
                                callBack.onFailureByNotExistCourse(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;
                        }
                    } catch (Exception e) {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnacceptedRole("");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                callBack.onCannotContactWithServer();
            }
        });
    }
}
