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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Spinner;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.adapters.QuizAdapter;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.QuizFragmentContract;
import com.example.tqt_quiz.presentation.database.DatabaseHelper;
import com.example.tqt_quiz.presentation.presenter.QuizFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;
import com.example.tqt_quiz.presentation.view.activities.CreateQuiz;
import com.example.tqt_quiz.presentation.view.activities.ViewQuiz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizFragment extends Fragment implements QuizFragmentContract.IView
{
    QuizFragmentContract.IPresenter presenter;
    private List<Quiz> quizList = new ArrayList<>();
    private QuizAdapter quizAdapter;
    private ListView lvQuiz;
    private ActivityResultLauncher<Intent> viewQuizLauncher, createQuizLauncher;
    private Button btnAddQuiz;
    private Spinner spnFilterCourse, spnFilterStatus;
    private List<String> courseIds;
    private List<String> statusOptions;
    private DatabaseHelper dbHelper;


    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        presenter = new QuizFragmentPresenter(this);

        //Ánh xạ
        lvQuiz = view.findViewById(R.id.lv_Quiz_Quiz);
        btnAddQuiz = view.findViewById(R.id.btn_Add_Quiz);
        spnFilterCourse = view.findViewById(R.id.spn_FilterCourse_Quiz);
        spnFilterStatus = view.findViewById(R.id.spn_FilterStatus_Quiz);

        dbHelper = new DatabaseHelper(requireContext());
        allQuizList = dbHelper.getAllQuizzes();
        quizList = new ArrayList<>(allQuizList);

        // Lấy danh sách courseID duy nhất từ quizList
        courseIds = new ArrayList<>();
        courseIds.add("Tất cả");

        for (Quiz quiz : quizList) {
            String cid = quiz.getCourseID();
            if (cid != null && !cid.isEmpty() && !courseIds.contains(cid)) {
                courseIds.add(cid);
            }
        }

        // Lựa chọn lọc theo tình trạng
        statusOptions = new ArrayList<>();
        statusOptions.add("Tất cả");
        statusOptions.add("Sắp diễn ra");
        statusOptions.add("Đang diễn ra");
        statusOptions.add("Đã kết thúc");

        // Set adapter cho spinners
        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, courseIds);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilterCourse.setAdapter(courseAdapter);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilterStatus.setAdapter(statusAdapter);

        //Set adapter cho 2 Spinner
        spnFilterCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterQuizList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spnFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterQuizList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        quizAdapter = new QuizAdapter(requireContext(), R.layout.item_quiz, quizList);
        lvQuiz.setAdapter(quizAdapter);

        //Khai báo launcher
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

    //Lọc theo courseID
    private void filterQuizList() {
        String selectedCourse = spnFilterCourse.getSelectedItem().toString();
        String selectedStatus = spnFilterStatus.getSelectedItem().toString();

        List<Quiz> filteredList = new ArrayList<>();

        for (Quiz quiz : allQuizList) {
            boolean matchCourse = selectedCourse.equals("Tất cả") ||
                    (quiz.getCourseID() != null && quiz.getCourseID().equals(selectedCourse));

            String quizStatus = getQuizStatus(quiz);
            boolean matchStatus = selectedStatus.equals("Tất cả") || selectedStatus.equals(quizStatus);

            if (matchCourse && matchStatus) {
                filteredList.add(quiz);
            }
        }

        quizList.clear();
        quizList.addAll(filteredList);
        quizAdapter.notifyDataSetChanged();
    }

    // Lọc trạng thái
    private String getQuizStatus(Quiz quiz) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();
            Date start = sdf.parse(quiz.getStartTime());
            Date end = sdf.parse(quiz.getDueTime());

            if (now.before(start)) return "Sắp diễn ra";
            else if (now.after(end)) return "Đã kết thúc";
            else return "Đang diễn ra";
        } catch (ParseException e) {
            return "Không rõ";
        }
    }


}