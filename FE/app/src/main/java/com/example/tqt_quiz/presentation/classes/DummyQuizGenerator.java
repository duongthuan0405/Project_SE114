package com.example.tqt_quiz.presentation.utils;

import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DummyQuizGenerator {

    public static List<Question> getSampleQuestions() {
        List<Question> questions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Question q = new Question();
            q.setId(UUID.randomUUID().toString());
            q.setContent("Câu hỏi số " + i);
            List<Answer> answers = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Answer a = new Answer();
                a.setId(UUID.randomUUID().toString());
                a.setContent("Đáp án " + (char) ('A' + j));
                a.setCorrect(j == 1); // luôn chọn đáp án B là đúng
                answers.add(a);
            }
            q.setAnswers(answers);
            questions.add(q);
        }
        return questions;
    }
}
