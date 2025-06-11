package com.example.tqt_quiz.data.repository.Token;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tqt_quiz.domain.repository.Token.ITokenManager;
public class TokenManager implements ITokenManager {
    private static final String PREF_NAME = "TokenPref";

    private static final String KEY_TOKEN = "jwt_ACCESS_token";

    private SharedPreferences prefs;



    public TokenManager(Context context) {

        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }



    public void SaveToken(String token) {

        prefs.edit().putString(KEY_TOKEN, token).apply();

    }



    public String GetToken() {

        return prefs.getString(KEY_TOKEN, null);

    }



    public void ClearToken() {

        prefs.edit().remove(KEY_TOKEN).apply();

    }
}
