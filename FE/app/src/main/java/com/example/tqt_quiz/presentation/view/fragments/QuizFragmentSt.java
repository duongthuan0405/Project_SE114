package com.example.tqt_quiz.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.adapters.CourseAdapterForSpinner;
import com.example.tqt_quiz.presentation.adapters.QuizAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.IReloadableTab;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.QuizFragmentStContract;
import com.example.tqt_quiz.presentation.presenter.QuizFragmentStPresenter;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.presentation.view.activities.ViewQuizSt;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizFragmentSt extends Fragment implements QuizFragmentStContract.IView, IReloadableTab
{
    QuizFragmentStContract.IPresenter presenter;
    private List<Quiz> quizList = new ArrayList<>();
    private QuizAdapter quizAdapter;
    private ListView lvQuiz;
    private ActivityResultLauncher<Intent> viewQuizLauncher;
    private Button btnAddQuiz;
    private Spinner spnFilterCourse, spnFilterStatus;
    private List<String> statusOptions;
    private List<Quiz> allQuizList;
    private List<Course> listCourse;


    public QuizFragmentSt() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        presenter = new QuizFragmentStPresenter(this);

        //Ánh xạ
        lvQuiz = view.findViewById(R.id.lv_Quiz_Quiz);
        btnAddQuiz = view.findViewById(R.id.btn_Add_Quiz);
        spnFilterCourse = view.findViewById(R.id.spn_FilterCourse_Quiz);
        spnFilterStatus = view.findViewById(R.id.spn_FilterStatus_Quiz);

        //Khai báo launcher
        viewQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );


        loadSpinner();

        AdapterView.OnItemSelectedListener lsn = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterQuizList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };


        //Set adapter cho 2 Spinner
        spnFilterCourse.setOnItemSelectedListener(lsn);
        spnFilterStatus.setOnItemSelectedListener(lsn);

        btnAddQuiz.setVisibility(View.GONE);

        lvQuiz.setOnItemClickListener((parent, view1, position, id) -> {
            Quiz selectedQuiz = (Quiz) lvQuiz.getAdapter().getItem(position);
            Intent intent = new Intent(requireContext(), ViewQuizSt.class);

            intent.putExtra("quizId", selectedQuiz.getId());
            viewQuizLauncher.launch(intent);
        });


        return view;
    }

    private void loadSpinner()
    {
        statusOptions = new ArrayList<>();
        statusOptions.add(StaticClass.StateOfQuiz.ALL);
        statusOptions.add(StaticClass.StateOfQuiz.SOON);
        statusOptions.add(StaticClass.StateOfQuiz.NOW);
        statusOptions.add(StaticClass.StateOfQuiz.END);
        ArrayAdapter<String> statusAdt = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusOptions);
        statusAdt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFilterStatus.setAdapter(statusAdt);
        presenter.loadCourseToSpinner();
        spnFilterStatus.setSelection(0);
    }

    private void filterQuizList() {

        if(spnFilterCourse.getSelectedItem() == null || spnFilterStatus.getSelectedItem() == null )
        {
            return;
        }

        String quizId = ((Course)spnFilterCourse.getSelectedItem()).getId();
        String status = spnFilterStatus.getSelectedItem().toString();

        if(quizId.equals(""))
        {
            presenter.getAllQuizByFilter(status);
        }

        else
        {
            presenter.getQuizByFilter(quizId, status);
        }


    }
    private String getQuizStatus(Quiz quiz) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date now = new Date();
            Date start = sdf.parse(quiz.getStartTime());
            Date end = sdf.parse(quiz.getDueTime());

            if (now.before(start)) return StaticClass.StateOfQuiz.SOON;
            else if (now.after(end)) return StaticClass.StateOfQuiz.END;
            else return StaticClass.StateOfQuiz.NOW;
        } catch (ParseException e) {
            return "Không rõ";
        }
    }


    @Override
    public void onTabReload() {
        presenter.loadCourseToSpinner();
    }

    @Override
    public Context getTheContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showOnCourseSpinner(List<Course> response) {
        listCourse = response;
        CourseAdapterForSpinner courseAdapterForSpinner = new CourseAdapterForSpinner(getTheContext(), listCourse);
        spnFilterCourse.setAdapter(courseAdapterForSpinner);
        spnFilterCourse.setSelection(0);
    }

    @Override
    public void navigateToLogin() {
        FragmentActivity parent = getActivity();
        Intent i = new Intent(getTheContext(), Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(getTheContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showQuiz(List<QuizDTO> quizzes) {
        allQuizList = new ArrayList<>();
        for(QuizDTO quizDTO : quizzes)
        {
            allQuizList.add(new Quiz(quizDTO));
        }

        quizAdapter = new QuizAdapter(getTheContext(), R.layout.item_quiz, allQuizList);
        lvQuiz.setAdapter(quizAdapter);
    }
}