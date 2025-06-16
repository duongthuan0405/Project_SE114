package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Course implements Serializable {
    private String name;
    private String description;
    private boolean isPrivate;
    private int avatar;
    private String hostName;

    public Course(String name, String description, boolean isPrivate, int avatar, String hostName) {
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.avatar = avatar;
        this.hostName = hostName;
    }

    public Course(String name, String hostName, int img) {
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPrivate() { return isPrivate; }
    public int getAvatar() { return avatar; }
    public String getHostName() { return hostName; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }
    public void setAvatar(int avatar) { this.avatar = avatar; }
    public void setHostName(String hostName) { this.hostName = hostName; }
}
