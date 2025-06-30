package com.example.tqt_quiz.data.interactor;

import android.content.Context;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.AnswerSelectService;
import com.example.tqt_quiz.domain.interactor.AnswerSelectInteractor;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerSelectInteractorIMP implements AnswerSelectInteractor {
    @Override
    public void SelectAnswer(String AttemptQuizId, String AnswerId, Context context, SelectAnswerCallBack callback) {
        TokenManager tokenManager=new TokenManager(context);
        AnswerSelectService service= RetrofitClient.GetClient(tokenManager).create(AnswerSelectService.class);
        Call<Void> call= service.AnswerSelect(AttemptQuizId,AnswerId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful())
                {
                    callback.onSuccess();
                }
                else
                {
                    String rawJson = "";
                    try
                    {
                        int code = response.code();
                        rawJson=response.errorBody().string();
                        JSONObject obj=new JSONObject(rawJson);
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
}
