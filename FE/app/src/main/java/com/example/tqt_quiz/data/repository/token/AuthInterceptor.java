package com.example.tqt_quiz.data.repository.token;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final com.example.tqt_quiz.data.repository.token.TokenManager tokenManager;

    public AuthInterceptor(com.example.tqt_quiz.data.repository.token.TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Request original = chain.request();
        String Token= tokenManager.GetToken();
        Request.Builder builder=original.newBuilder();
        if(Token!=null)
        {
           builder.header("Authorization","Bearer " + Token);
        }
        Request authorized=builder.build();
        return chain.proceed(authorized);
    }
}
