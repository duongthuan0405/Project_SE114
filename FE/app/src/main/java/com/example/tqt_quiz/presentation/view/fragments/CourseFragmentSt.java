package com.example.tqt_quiz.presentation.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.adapters.CourseAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.IReloadableTab;
import com.example.tqt_quiz.presentation.contract_vp.CourseFragmentStContract;
import com.example.tqt_quiz.presentation.presenter.CourseFragmentStPresenter;
import com.example.tqt_quiz.presentation.view.activities.JoinCourseActivitySt;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.presentation.view.activities.ViewCourse;
import com.example.tqt_quiz.presentation.view.activities.ViewCourseSt;

import java.util.ArrayList;
import java.util.List;

public class CourseFragmentSt extends Fragment implements CourseFragmentStContract.IView, IReloadableTab {

    private RadioButton rdbMembers, rdbPending;
    private ListView lvCourses;
    private List<Course> courseList = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private CourseFragmentStContract.IPresenter presenter;
    private EditText edTx_FindCourse;
    private Button btnJoinCourse;
    private ActivityResultLauncher<Intent> joinCourseLauncher;
    private ActivityResultLauncher<Intent> viewCourseLauncher;

    public CourseFragmentSt() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CourseFragmentStPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_st, container, false);

        rdbMembers = view.findViewById(R.id.rdb_Course_ViewCourseSt);
        rdbPending = view.findViewById(R.id.rdb_Pending_ViewCourseSt);
        lvCourses = view.findViewById(R.id.lv_Course_CourseSt);
        edTx_FindCourse = view.findViewById(R.id.edt_Find_CourseSt);
        btnJoinCourse = view.findViewById(R.id.btn_Join_CourseSt);

        // Mặc định chọn "Khóa học"
        rdbMembers.setChecked(true);
        presenter.loadJoinedCourses();

        rdbMembers.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.loadJoinedCourses();
            }
        });

        rdbPending.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.loadPendingCourses();
            }
        });

        joinCourseLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    onTabReload();
                });

        viewCourseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                o -> onTabReload());


        lvCourses.setOnItemClickListener((parent, view1, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(requireContext(), ViewCourseSt.class);
            intent.putExtra("courseId", selectedCourse.getId());
            viewCourseLauncher.launch(intent);
        });

        btnJoinCourse.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), JoinCourseActivitySt.class);
            joinCourseLauncher.launch(intent);
        });

        edTx_FindCourse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (courseAdapter != null) {
                    courseAdapter.filtCourse(s.toString());
                }
            }
        });


        return view;
    }

    @Override
    public Context getTheContext() {
        return requireContext().getApplicationContext();
    }

    @Override
    public void showCourseList(List<CourseDTO> response) {
        courseList.clear();
        for (CourseDTO dto : response) {
            courseList.add(new Course(dto));
        }

        courseAdapter = new CourseAdapter(requireContext(), R.layout.item_course, courseList);
        lvCourses.setAdapter(courseAdapter);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTabReload() {
        if(rdbMembers.isChecked())
        {
            presenter.loadJoinedCourses();
        }
        else
        {
            presenter.loadPendingCourses();
        }
    }
}
