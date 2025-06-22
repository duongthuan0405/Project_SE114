package com.example.tqt_quiz.presentation.view.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.adapters.QuizAdapter;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.QuizFragmentContract;
import com.example.tqt_quiz.presentation.database.DatabaseHelper;
import com.example.tqt_quiz.presentation.presenter.QuizFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;
import com.example.tqt_quiz.presentation.view.activities.CreateQuiz;
import com.example.tqt_quiz.presentation.view.activities.ViewQuiz;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment implements QuizFragmentContract.IView
{
    QuizFragmentContract.IPresenter presenter;
    private List<Quiz> quizList = new ArrayList<>();
    private QuizAdapter quizAdapter;
    private ListView lvQuiz;
    private ActivityResultLauncher<Intent> viewQuizLauncher, createQuizLauncher;
    private Button btnAddQuiz;


    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        presenter = new QuizFragmentPresenter(this);

        lvQuiz = view.findViewById(R.id.lv_Quiz_Quiz);
        btnAddQuiz = view.findViewById(R.id.btn_Add_Quiz);


        viewQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        createQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Toast.makeText(getContext(), "Thêm quiz Ok", Toast.LENGTH_LONG).show();
                    }
                }
        );

        lvQuiz.setOnItemClickListener((parent, view1, position, id) -> {
            Quiz quiz = quizList.get(position);


            Intent intent = new Intent(requireContext(), CreateQuiz.class);
            String quiz_id = ((Quiz)lvQuiz.getAdapter().getItem(position)).getId();
            intent.putExtra("quizId", quiz_id);
            createQuizLauncher.launch(intent);
        });


        btnAddQuiz.setOnClickListener(v -> {

            Intent intent = new Intent(requireContext(), CreateQuiz.class);
            createQuizLauncher.launch(intent);
        });


        return view;
    }
}