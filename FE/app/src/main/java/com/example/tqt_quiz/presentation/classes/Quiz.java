package com.example.tqt_quiz.presentation.classes;

import java.io.Serializable;

public class Quiz implements Serializable {
    private String name;
    private String description;
    private String startTime;
    private String dueTime;

    public Quiz(String name, String description, String startTime, String dueTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.dueTime = dueTime;
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
}
