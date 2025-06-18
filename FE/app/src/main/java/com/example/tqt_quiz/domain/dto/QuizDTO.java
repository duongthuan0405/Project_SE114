package com.example.tqt_quiz.domain.dto;

import com.example.tqt_quiz.domain.JasonAdapters.LocalDateTimeAdapter;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.JsonAdapter;

import java.time.LocalDateTime;

public class QuizDTO {

    @SerializedName("id")
    private String id;

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

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("isPublished")
    private String isPublished;

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }
    public void setDueTime(LocalDateTime dueTime) {
        this.dueTime = dueTime;
    }

    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getIsPublished() {
        return isPublished;
    }
    public void setIsPublished(String isPublished) {
        this.isPublished = isPublished;
    }
}
