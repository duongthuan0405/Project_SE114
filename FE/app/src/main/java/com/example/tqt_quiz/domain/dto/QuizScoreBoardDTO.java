package com.example.tqt_quiz.domain.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizScoreBoardDTO {
    @SerializedName("quiz")
    QuizDTO quiz;

    @SerializedName("scores")
    List<AccountWithScore> scores;

    public QuizDTO getQuiz() {
        return quiz;
    }

    public List<AccountWithScore> getScores() {
        return scores;
    }
}