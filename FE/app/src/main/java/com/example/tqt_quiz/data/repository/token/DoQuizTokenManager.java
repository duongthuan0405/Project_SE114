package com.example.tqt_quiz.data.repository.token;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tqt_quiz.domain.repository.token.ITokenManager;

public class DoQuizTokenManager implements ITokenManager {
    private static final String PREF_NAME = "DoQUizTokenPref";

    private static final String KEY_TOKEN = "jwt_DoQuiz_token";

    private SharedPreferences prefs;
    public DoQuizTokenManager(Context context) {

        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }
    @Override
    public void SaveToken(String Token) {
        prefs.edit().putString(KEY_TOKEN,Token).apply();
    }

    @Override
    public String GetToken() {
        return prefs.getString(KEY_TOKEN,"");
    }

    @Override
    public void ClearToken() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
