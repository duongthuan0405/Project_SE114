package com.example.tqt_quiz.data.interactor;

import android.content.Context;
import android.util.Log;

import com.example.tqt_quiz.data.repository.token.RetrofitClient;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.APIService.CreateQuizService;
import com.example.tqt_quiz.domain.APIService.FetchQuizService;
import com.example.tqt_quiz.domain.APIService.PublishQuizService;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRelatedIMP implements IQuizRelatedInteract {

    @Override
    public void SearchQuizById(String id, Context context, SearchQuizCallBack callBack) {
        TokenManager tokenManager=new TokenManager(context);
        FetchQuizService service= RetrofitClient.GetClient(tokenManager).create(FetchQuizService.class);
        Call<QuizDTO> call= service.FetchQuizByQuizID();
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
            public void onFailure(Call<List<QuizDTO>> call, Throwable t) {
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

}
