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
    private List<Quiz> quizList;
    private QuizAdapter quizAdapter;
    private ListView lvQuiz;
    private ActivityResultLauncher<Intent> viewQuizLauncher, createQuizLauncher;
    private Button btnAddQuiz;
    private DatabaseHelper dbHelper;


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

        dbHelper = new DatabaseHelper(requireContext());

        quizList = dbHelper.getAllQuizzes();

        quizAdapter = new QuizAdapter(requireContext(), R.layout.item_quiz, quizList);
        lvQuiz.setAdapter(quizAdapter);

        viewQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        createQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        quizList.clear();
                        quizList.addAll(dbHelper.getAllQuizzes());
                        quizAdapter.notifyDataSetChanged();
                    }
                }
        );

        lvQuiz.setOnItemClickListener((parent, view1, position, id) -> {
            Quiz quiz = quizList.get(position);

            // ----- GHI CHÚ: Đoạn này dùng để mở ViewQuiz (đã tắt) -----
            /*
            Intent intent = new Intent(requireContext(), ViewQuiz.class);
            intent.putExtra("quiz_name", quiz.getName());
            intent.putExtra("quiz_description", quiz.getDescription());
            intent.putExtra("quiz_start", quiz.getStartTime());
            intent.putExtra("quiz_due", quiz.getDueTime());
            viewQuizLauncher.launch(intent);


            // ----- Mở CreateCourse thay thế -----
            Intent intent = new Intent(requireContext(), CreateQuiz.class);
            intent.putExtra("quiz_name", quiz.getName());
            intent.putExtra("quiz_description", quiz.getDescription());
            intent.putExtra("quiz_start", quiz.getStartTime());
            intent.putExtra("quiz_due", quiz.getDueTime());
            intent.putExtra("quiz_is_public", quiz.isPublished());

             */
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