package com.example.tqt_quiz.data.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.CreateQuestionService;
import com.example.tqt_quiz.domain.APIService.FetchQuestionService;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.interactor.IQuestionrelatedInteract;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionrealatedIMP implements IQuestionrelatedInteract {

    @Override
    public void CreateQuestion(List<CreateQuestionRequest> questionRequest, Context context, CreateQuestionCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        CreateQuestionService service= RetrofitClient.GetClient(tokenManager).create(CreateQuestionService.class);
        Call<Void> call= service.CreateQuestion(questionRequest);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess();
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
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void FetchQuizQuestionForTeacher(String quizId, Context context, FetchQuizQuestionForTeacherCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuestionService service= RetrofitClient.GetClient(tokenManager).create(FetchQuestionService.class);
        Call<List<QuestionDTO>> call= service.FetchQuizQuestionForTeacher(quizId);
        call.enqueue(new Callback<List<QuestionDTO>>() {
            @Override
            public void onResponse(Call<List<QuestionDTO>> call, Response<List<QuestionDTO>> response) {
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
            public void onFailure(Call<List<QuestionDTO>> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }

    @Override
    public void FetchQuizQuestionForStudent(String quizId, Context context, FetchQuizQuestionForStudentCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuestionService service= RetrofitClient.GetClient(tokenManager).create(FetchQuestionService.class);
        Call<List<QuestionDTO>> call= service.FetchQuizQuestionForStudent(quizId);
        call.enqueue(new Callback<List<QuestionDTO>>() {
            @Override
            public void onResponse(Call<List<QuestionDTO>> call, Response<List<QuestionDTO>> response) {
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
            public void onFailure(Call<List<QuestionDTO>> call, Throwable t) {
                    callback.onFailureByCannotSendToServer();
            }
        });
    }
}
