package com.example.tqt_quiz.presentation.classes;

import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Quiz implements Serializable {
    private String id;
    private String name;
    private String description;
    private String startTime;
    private String dueTime;
    private boolean isPublished;
    private String courseID;

    public Quiz(String name, String description, String startTime, String dueTime, boolean isPublished) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.dueTime = dueTime;
        this.isPublished = isPublished;
    }

    public Quiz(String id, String name, String description, String startTime, String dueTime, boolean isPublished, String courseID) {
        this(name, description, startTime, dueTime, isPublished);
        this.id = id;
        this.courseID = courseID;
    }

    public Quiz(QuizDTO quizDTO)
    {
        this.name = quizDTO.getName();
        this.description = quizDTO.getDescription();
        this.startTime = quizDTO.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.dueTime = quizDTO.getDueTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.isPublished = quizDTO.getIsPublished();
        this.id = quizDTO.getId();
        this.courseID = quizDTO.getCourseId();
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

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String idCourse) {
        this.courseID = idCourse;
    }

    public LocalDateTime getLCDT_StartTime()
    {
        return LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat));
    }

    public LocalDateTime getLCDT_DueTime()
    {
        return LocalDateTime.parse(dueTime, DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat));
    }
}
