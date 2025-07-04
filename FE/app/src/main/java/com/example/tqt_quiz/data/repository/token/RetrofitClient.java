package com.example.tqt_quiz.data.repository.token;

import com.example.tqt_quiz.data.repository.token.AuthInterceptor;
import com.example.tqt_quiz.data.repository.token.TokenManager;
import com.example.tqt_quiz.domain.repository.token.ITokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL="http://10.0.2.2:5027";
    private static Retrofit retrofit=null;
    public static Retrofit GetClient(ITokenManager tokenManager)
    {
        if(retrofit==null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .build();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).
                    client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
