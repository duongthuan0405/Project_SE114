package com.example.tqt_quiz.domain.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class LoginResponse {
    @SerializedName("token")
    @Nullable
    private String token;

    @SerializedName("userId")
    @Nullable
    private String userID;

    @SerializedName("fullName")
    @Nullable
    private String fullName;

    @SerializedName("role")
    @Nullable
    private String role;

    @SerializedName("roleId")
    @Nullable
    private String roleId;

    // Getter and Setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getter and Setter for userID
    @Nullable
    public String getUserID() {
        return userID;
    }

    public void setUserID(@Nullable String userID) {
        this.userID = userID;
    }

    // Getter and Setter for fullName
    @Nullable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@Nullable String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for role
    @Nullable
    public String getRole() {
        return role;
    }

    public void setRole(@Nullable String role) {
        this.role = role;
    }


    // Getter and Setter for roleId
    @Nullable
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(@Nullable String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", userID='" + userID + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", roleId='" + roleId + '\'' +
                '}';
    }
}

