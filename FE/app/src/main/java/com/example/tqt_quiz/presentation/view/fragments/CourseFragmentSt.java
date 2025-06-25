package com.example.tqt_quiz.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.adapters.CourseAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.contract_vp.ICourseFragmentStContract;
import com.example.tqt_quiz.presentation.presenter.CourseFragmentStPresenter;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.presentation.view.activities.ViewCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseFragmentSt extends Fragment implements ICourseFragmentStContract.IView {

    private RadioButton rdbMembers, rdbPending;
    private ListView lvCourses;
    private List<Course> courseList = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private ICourseFragmentStContract.IPresenter presenter;
    private EditText edTx_FindCourse;

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

        rdbMembers = view.findViewById(R.id.rdb_Members_ViewCourseSt);
        rdbPending = view.findViewById(R.id.rdb_Pending_ViewCourseSt);
        lvCourses = view.findViewById(R.id.lv_Course_CourseSt);
        edTx_FindCourse = view.findViewById(R.id.edt_Find_CourseSt);

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

        lvCourses.setOnItemClickListener((parent, view1, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(requireContext(), ViewCourse.class);
            intent.putExtra("courseId", selectedCourse.getId());
            startActivity(intent);
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
}
