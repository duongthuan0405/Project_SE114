package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Quiz implements Serializable {
    private String id;
    private String name;
    private String description;
    private String startTime;
    private String dueTime;
    private boolean isPublished;

    public Quiz(String name, String description, String startTime, String dueTime, boolean isPublished) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.dueTime = dueTime;
        this.isPublished = isPublished;
    }

    public Quiz(String id, String name, String description, String startTime, String dueTime, boolean isPublished) {
        this(name, description, startTime, dueTime, isPublished);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDueTime() {
        return dueTime;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }
}
