package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class AccountInfo {
    @SerializedName("userId")
    private String UserID;

    // Getter
    public String getUserID() {
        return UserID;
    }

    // Setter
    public void setUserID(String userID) {
        this.UserID = userID;
    }
}
