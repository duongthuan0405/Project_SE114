package com.example.tqt_quiz.data.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.SubmitQuizService;
import com.example.tqt_quiz.domain.interactor.ISubmitQuizInteractor;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitQuizInteractorIMP implements ISubmitQuizInteractor {

    @Override
    public void SubmitQuiz(String quizId, Context context, SubmitQuizCallBack callback) {
        TokenManager tokenManager= new TokenManager(context);
        SubmitQuizService service= RetrofitClient.GetClient(tokenManager).create(SubmitQuizService.class);
        Call<Void> call= service.SubmitQuiz(quizId);
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
}
