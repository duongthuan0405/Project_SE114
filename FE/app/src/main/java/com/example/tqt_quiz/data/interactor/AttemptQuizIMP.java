package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.DoQuizTokenManager;
import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.AttemptQuizService;
import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.interactor.IAttemptQuizInteract;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttemptQuizIMP implements IAttemptQuizInteract {

    @Override
    public void AttemptQuiz(String quizId, Context context, AtttemptQuizCallBack callback) {
        DoQuizTokenManager tokenManager=new DoQuizTokenManager(context);
        DoQuizTokenManager doQuizTokenManager=new DoQuizTokenManager(context);
        AttemptQuizService service= RetrofitClient.GetClient(tokenManager).create(AttemptQuizService.class);
        Call<AttemptQuizDTO> call= service.AttemptQuiz(quizId);
        call.enqueue(new Callback<AttemptQuizDTO>() {
            @Override
            public void onResponse(Call<AttemptQuizDTO> call, Response<AttemptQuizDTO> response) {
                if(response.isSuccessful())
                {
                    doQuizTokenManager.SaveToken(response.body().getTokenForQuiz());
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
            public void onFailure(Call<AttemptQuizDTO> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }
    @Override
    public void GetAttemptInfo(String quizId, Context context, GetAttemptInfo callback) {
        TokenManager tokenManager=new TokenManager(context);
        AttemptQuizService service= RetrofitClient.GetClient(tokenManager).create(AttemptQuizService.class);
        Call<AttemptQuizDTO> call= service.GetAttemptInfo(quizId);
        call.enqueue(new Callback<AttemptQuizDTO>() {
            @Override
            public void onResponse(Call<AttemptQuizDTO> call, Response<AttemptQuizDTO> response) {

                if(response.isSuccessful())
                {
                    callback.onSuccess(response.body());
                }
                else{
                    if(response.code() == 404)
                    {
                        callback.onNotAttemptYet();
                        return;
                    }
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
            public void onFailure(Call<AttemptQuizDTO> call, Throwable t) {
                callback.onFailureByCannotSendToServer();
            }
        });
    }
}
