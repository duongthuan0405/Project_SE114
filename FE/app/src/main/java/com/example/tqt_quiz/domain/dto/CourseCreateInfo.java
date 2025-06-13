package com.example.tqt_quiz.domain.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CourseCreateInfo {
    @SerializedName("name")
    @NonNull
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("isPrivate")
    private String isPrivate;

    @SerializedName("avatar")
    private String avatar;

    // Getter and Setter for name
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for isPrivate
    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    // Getter and Setter for avatar
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

