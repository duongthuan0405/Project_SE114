package com.example.tqt_quiz.domain.dto;

import com.example.tqt_quiz.domain.JasonAdapters.LocalDateTimeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class QuizCreateRequestDTO {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("startTime")
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime startTime;

    @SerializedName("dueTime")
    @JsonAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime dueTime;

    @SerializedName("courseId")
    private String courseId;

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for startTime
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    // Getter and Setter for dueTime
    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    // Getter and Setter for courseId
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}

