package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordDTO {
    @SerializedName("oldPassword")
    String oldPassword;
    @SerializedName("newPassword")
    String newPassword;
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
