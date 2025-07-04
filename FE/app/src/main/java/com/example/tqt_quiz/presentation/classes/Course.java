package com.example.tqt_quiz.presentation.classes;

import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.io.Serializable;

public class Course implements Serializable {
    private String id;
    private String name;
    private String description;
    private boolean isPrivate;
    private String avatar;
    private String hostName;

    public Course(String id, String name, String description, boolean isPrivate, String avatar, String hostName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPrivate = isPrivate;
        this.avatar = avatar;
        this.hostName = hostName;
    }

    public Course(CourseDTO courseDTO)
    {
        id = courseDTO.getId();
        name = courseDTO.getName();
        description = courseDTO.getDescription();
        isPrivate = courseDTO.isPrivate();
        avatar = courseDTO.getAvatar();
        hostName = courseDTO.getHostName();
    }

    public Course(String name, String hostName, int img) {
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPrivate() { return isPrivate; }
    public String getAvatar() { return avatar; }
    public String getHostName() { return hostName; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setHostName(String hostName) { this.hostName = hostName; }

}
