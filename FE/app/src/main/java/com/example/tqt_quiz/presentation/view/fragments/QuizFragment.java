package com.example.tqt_quiz.presentation.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.adapters.QuizAdapter;
import com.example.tqt_quiz.presentation.classes.Quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;
    private ListView lvQuiz;

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        lvQuiz = view.findViewById(R.id.lv_Quiz_Quiz);

        quizList = new ArrayList<>();
        quizList.add(new Quiz("Quiz 1", "Mô tả 1", "2025-06-10 08:00", "2025-06-10 10:00"));
        quizList.add(new Quiz("Quiz 2", "Mô tả 2", "2025-06-11 14:00", "2025-06-11 16:00"));

        quizAdapter = new QuizAdapter(requireContext(), R.layout.item_quiz, quizList);
        lvQuiz.setAdapter(quizAdapter);

        return view;
    }
}