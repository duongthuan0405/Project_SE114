package com.example.tqt_quiz.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tqt_quiz.domain.repository.IRoleManager;

public class RoleManager implements IRoleManager {
    private static String PrefName="RolePref";
    private static String KeyRole="RoleKey";
    private SharedPreferences pref;
    public RoleManager(Context context)
    {
        pref=context.getSharedPreferences(PrefName,Context.MODE_PRIVATE);
    }
    @Override
    public void SaveRole(String RoleID) {
        pref.edit().putString(KeyRole,RoleID);
    }

    @Override
    public String GetRole() {
        return pref.getString(KeyRole,"");
    }

    @Override
    public void ClearRole() {
        pref.edit().remove(KeyRole).apply();
    }
}
