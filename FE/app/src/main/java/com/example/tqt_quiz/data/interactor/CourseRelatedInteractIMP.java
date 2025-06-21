package com.example.tqt_quiz.data.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.CreateNewCourseService;
import com.example.tqt_quiz.domain.APIService.FetchAllUserCourseService;
import com.example.tqt_quiz.domain.APIService.FindCourseService;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRelatedInteractIMP implements ICourseRelatedInteract {
    @Override
    public void FetchAllCourseJoined(Context context, FetchJoinedCallBack callBack)
    {
        TokenManager tokenManager=new TokenManager(context);
        FetchAllUserCourseService service= RetrofitClient.GetClient(tokenManager).create(FetchAllUserCourseService.class);
        Call<List<CourseDTO>> call= service.FetchAllCoruseJoined();
        call.enqueue(new Callback<List<CourseDTO>>()
        {

            @Override
            public void onResponse(Call<List<CourseDTO>> call, Response<List<CourseDTO>> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    String rawJson="";
                    try {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        switch (code)
                        {
                            case 404:
                                callBack.onFailureByNotExistAccount(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;

                        }
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CourseDTO>> call, Throwable t) {
                    callBack.onFailureByCannotSendToServer();
            }
        });
    }
    public void FetchAllCourseHosted(Context context,FetchHostedCallBack callBack)
    {
        TokenManager tokenManager=new TokenManager(context);
        FetchAllUserCourseService service= RetrofitClient.GetClient(tokenManager).create(FetchAllUserCourseService.class);
        Call<List<CourseDTO>> call= service.FetchAllCourseHosted();
        call.enqueue(new Callback<List<CourseDTO>>() {

            @Override
            public void onResponse(Call<List<CourseDTO>> call, Response<List<CourseDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    String rawJson="";
                    try {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        switch (code) {
                            case 404:
                                callBack.onFailureByNotExistAccount(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;

                        }
                    } catch (Exception e) {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CourseDTO>> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }
    public void FetchAllCoursePending(Context context,FetchAllPendingCallBack callBack)
    {
        TokenManager tokenManager= new TokenManager(context);
        FetchAllUserCourseService service= RetrofitClient.GetClient(tokenManager).create(FetchAllUserCourseService.class);
        Call<List<CourseDTO>> call= service.FetchAllCoursePending();
        call.enqueue(new Callback<List<CourseDTO>>() {

            @Override
            public void onResponse(Call<List<CourseDTO>> call, Response<List<CourseDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    String rawJson="";
                    try {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString("message");
                        switch (code) {
                            case 404:
                                callBack.onFailureByNotExistAccount(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;

                        }
                    } catch (Exception e) {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CourseDTO>> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void FetchAllCourseDenied(Context context, FetchAllDeniedCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        FetchAllUserCourseService service= RetrofitClient.GetClient(tokenManager).create(FetchAllUserCourseService.class);
        Call<List<CourseDTO>> call= service.FetchAllCourseDenied();
        call.enqueue(new Callback<List<CourseDTO>>() {

            @Override
            public void onResponse(Call<List<CourseDTO>> call, Response<List<CourseDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                } else {
                    String rawJson="";
                    try {
                        int code = response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        switch (code) {
                            case 404:
                                callBack.onFailureByNotExistAccount(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;

                        }
                    } catch (Exception e) {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CourseDTO>> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void FindCourseByID(String course_ID, Context context, FindCourseByIDCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        FindCourseService service=RetrofitClient.GetClient(tokenManager).create(FindCourseService.class);
        Call<CourseDTO> call=service.FindCourseByID(course_ID);
        call.enqueue(new Callback<CourseDTO>() {
            @Override
            public void onResponse(Call<CourseDTO> call, Response<CourseDTO> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else {
                    String rawJson="";
                    try
                    {
                        int code= response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg= obj.optString(rawJson);
                        switch (code)
                        {
                            case 404:
                                callBack.onFailureByNotExistCourse(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;
                        }
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CourseDTO> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void CreateNewCourse(CourseCreateInfo info, Context context,CreateNewCourseCallBack callBack)
    {
        TokenManager tokenManager=new TokenManager(context);
        CreateNewCourseService service=RetrofitClient.GetClient(tokenManager).create(CreateNewCourseService.class);
        Call<CourseDTO> call= service.Create(info);
        call.enqueue(new Callback<CourseDTO>() {
            @Override
            public void onResponse(Call<CourseDTO> call, Response<CourseDTO> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    String rawJson="";
                    try {
                        int code = response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=response.errorBody().string();
                        switch (code) {
                            case 404:
                                callBack.onFailureByNotExistAccount(msg);
                                break;
                            case 500:
                                callBack.onFailureByServerError(msg);
                                break;
                        }
                    }catch(Exception e){
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken("");
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole("");
                        e.printStackTrace();

                        }
                    }
                }


            @Override
            public void onFailure(Call<CourseDTO> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void GetAllMemberInCourse(String course_id, Context context, GetAllMemberInCourseCallBack callBack) {
        TokenManager tokenManager = new TokenManager(context);
        FetchAllUserCourseService retrofitClient = RetrofitClient.GetClient(tokenManager).create(FetchAllUserCourseService.class);
        retrofitClient.FetchAllMember(course_id).enqueue(new Callback<List<AccountInfo>>() {
            @Override
            public void onResponse(Call<List<AccountInfo>> call, Response<List<AccountInfo>> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else
                {
                    String rawJson = "";
                    try
                    {
                        int code = response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=response.errorBody().string();
                        callBack.onFailureByOtherError(msg);
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callBack.onFailureByExpiredToken();
                        else if(response.code() == 403)
                            callBack.onFailureByUnAcepptedRole();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AccountInfo>> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }
}
