package com.example.tqt_quiz.domain.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CourseDTO {
    @SerializedName("id")
    @NonNull
    private String id;

    @SerializedName("name")
    @NonNull
    private String name;

    @SerializedName("hostName")
    private String hostName;

    @SerializedName("hostId")
    private String hostId;

    @SerializedName("isPrivate")
    private boolean isPrivate;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("description")
    private String description;
    @SerializedName("stateOfJoining")
    private String stateOfJoining;

    public String getStateOfJoining() {
        return stateOfJoining;
    }

    public void setStateOfJoining(String stateOfJoining) {
        this.stateOfJoining = stateOfJoining;
    }

    // Getter and Setter for id
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    // Getter and Setter for name
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    // Getter and Setter for hostName
    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    // Getter and Setter for isPrivate
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    // Getter and Setter for avatar
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
}

