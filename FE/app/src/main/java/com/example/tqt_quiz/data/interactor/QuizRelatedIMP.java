package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.CreateQuizService;
import com.example.tqt_quiz.domain.APIService.DeleteQuizService;
import com.example.tqt_quiz.domain.APIService.FetchQuizService;
import com.example.tqt_quiz.domain.APIService.GetQuizRecordForTeeacherService;
import com.example.tqt_quiz.domain.APIService.GetQuizScoreService;
import com.example.tqt_quiz.domain.APIService.PublishQuizService;
import com.example.tqt_quiz.domain.APIService.UpdateQuizService;
import com.example.tqt_quiz.domain.dto.AccountWithScore;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.domain.repository.token.ITokenManager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class QuizRelatedIMP implements IQuizRelatedInteract {

    @Override
    public void SearchQuizById(String id, Context context, SearchQuizCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuizService service= RetrofitClient.GetClient(tokenManager).create(FetchQuizService.class);
        Call<QuizDTO> call= service.FetchQuizByQuizID(id);
        call.enqueue(new Callback<QuizDTO>() {
            @Override
            public void onResponse(Call<QuizDTO> call, Response<QuizDTO> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else{
                    String rawJson="";
                    try
                    {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callBack.onOtherFailure(msg);
                    } catch (Exception e) {
                        if(response.code()==401)
                        {
                            callBack.onFailureByExpiredToken();
                        }
                        else if(response.code()==403)
                        {
                            callBack.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QuizDTO> call, Throwable t) {
                    callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void FetchALlQuizOfACourse(String Course_ID, Context context, FetchALlQuizOfACourseCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuizService service= RetrofitClient.GetClient(tokenManager).create(FetchQuizService.class);
        Call<List<QuizDTO>> call= service.FetchAllQuizByCourseID(Course_ID);

        call.enqueue(new Callback<List<QuizDTO>>() {
            @Override
            public void onResponse(Call<List<QuizDTO>> call, Response<List<QuizDTO>> response) {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body());
                }
                else {
                    String rawJson = "";
                    try {
                        rawJson = response.errorBody().string();
                        JSONObject obj = new JSONObject(rawJson);
                        String msg = obj.optString("message");
                        callBack.onOtherFailure(msg);
                    } catch (Exception e) {
                        if (response.code() == 401) {
                            callBack.onFailureByExpiredToken();
                        } else if (response.code() == 403) {
                            callBack.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QuizDTO>> call, Throwable t)
            {
                Log.e("THUAN", "onFailure – Retrofit call failed", t);
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void CreateQuiz(QuizCreateRequestDTO quiz, Context context, CreateQuizCallBack callBack) {

        TokenManager tokenManager=new TokenManager(context);
        CreateQuizService service= RetrofitClient.GetClient(tokenManager).create(CreateQuizService.class);
        Call<QuizDTO> call = null;
        call = service.CreateQuiz(quiz);

        call.enqueue(new Callback<QuizDTO>() {
            @Override
            public void onResponse(Call<QuizDTO> call, Response<QuizDTO> response) {

                if(response.isSuccessful())
                {
                    callBack.onSuccess(response.body());
                }
                else{
                    String rawJson="";
                    try
                    {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callBack.onOtherFailure(msg);
                    } catch (Exception e) {
                        if(response.code()==401)
                        {
                            callBack.onFailureByExpiredToken();
                        }
                        else if(response.code()==403)
                        {
                            callBack.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QuizDTO> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void PublishQuiz(String Quiz_id, Context context, PublishQuizCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        PublishQuizService service=RetrofitClient.GetClient(tokenManager).create(PublishQuizService.class);
        Call<Void> call=service.QuizPublish(Quiz_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callBack.onSuccess();
                }
                else{
                    String rawJson="";
                    try
                    {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callBack.onOtherFailure(msg);
                    } catch (Exception e) {
                        if(response.code()==401)
                        {
                            callBack.onFailureByExpiredToken();
                        }
                        else if(response.code()==403)
                        {
                            callBack.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void UpdateQuiz(String Quiz_id, QuizCreateRequestDTO updatedquiz, Context context, UpdateQuizCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        UpdateQuizService service=RetrofitClient.GetClient(tokenManager).create(UpdateQuizService.class);
        Log.d("THUANKK", updatedquiz.isPublished() ? "PUBLISH" : "NO");
        Call<QuizDTO> call=service.UpdateQuiz(Quiz_id, updatedquiz);
        call.enqueue(new Callback<QuizDTO>() {
            @Override
            public void onResponse(Call<QuizDTO> call, Response<QuizDTO> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess(response.body());
                }
                else{
                    String rawJson="";
                    try
                    {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    } catch (Exception e) {
                        if(response.code()==401)
                        {
                            callback.onFailureByExpiredToken();
                        }
                        else if(response.code()==403)
                        {
                            callback.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<QuizDTO> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void GetAllQuiz(Context context, GetAllQuizCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuizService service=RetrofitClient.GetClient(tokenManager).create(FetchQuizService.class);
        Call<List<QuizDTO>> call= service.FetchAllQuiz();
        call.enqueue(new Callback<List<QuizDTO>>() {
            @Override
            public void onResponse(Call<List<QuizDTO>> call, Response<List<QuizDTO>> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess(response.body());
                }
                else{
                    String rawJson="";
                    try
                    {
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    } catch (Exception e) {
                        if(response.code()==401)
                        {
                            callback.onFailureByExpiredToken();
                        }
                        else if(response.code()==403)
                        {
                            callback.onFailureByUnAcceptedRole();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<QuizDTO>> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void DeleteQuiz(String QuizID, Context context, DeleteQuizCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        DeleteQuizService service=RetrofitClient.GetClient(tokenManager).create(DeleteQuizService.class);
        Call<Void> call=service.DeleteQuiz(QuizID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess();
                }
                else
                {
                    try
                    {
                        String rawJSON="";
                        rawJSON=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJSON);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callback.onFailureByExpiredToken();
                        else if(response.code() == 403)
                            callback.onFailureByUnAcceptedRole();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                    callback.onFailureByCannotSendToServer();
            }
        });
    }
    @Override
    public void GetQuizScore(String QuizId, Context context, GetQuizScoreCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        GetQuizScoreService service=RetrofitClient.GetClient(tokenManager).create(GetQuizScoreService.class);
        Call<QuizWithScoreDTO> call= service.GetQuizScore(QuizId);
        call.enqueue(new Callback<QuizWithScoreDTO>() {
            @Override
            public void onResponse(Call<QuizWithScoreDTO> call, Response<QuizWithScoreDTO> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess(response.body());
                }
                else
                {
                    try
                    {
                        String rawJSON="";
                        rawJSON=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJSON);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callback.onFailureByExpiredToken();
                        else if(response.code() == 403)
                            callback.onFailureByUnAcceptedRole();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuizWithScoreDTO> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void GetQuizRecordForTeeacher(String quizid, Context context, GetQuizRecordForTeeacherCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        GetQuizRecordForTeeacherService service=RetrofitClient.GetClient(tokenManager).create(GetQuizRecordForTeeacherService.class);
        Call<List<AccountWithScore>> call= service.GetQuizRecordForTeeacher(quizid);
        call.enqueue(new Callback<List<AccountWithScore>>() {
            @Override
            public void onResponse(Call<List<AccountWithScore>> call, Response<List<AccountWithScore>> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess(response.body());
                }
                else
                {
                    try
                    {
                        String rawJSON="";
                        rawJSON=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJSON);
                        String msg=obj.optString("message");
                        callback.onOtherFailure(msg);
                    }
                    catch (Exception e)
                    {
                        if(response.code() == 401)
                            callback.onFailureByExpiredToken();
                        else if(response.code() == 403)
                            callback.onFailureByUnAcceptedRole();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<AccountWithScore>> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }
}
