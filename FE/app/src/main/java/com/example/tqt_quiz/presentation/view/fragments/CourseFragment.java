package com.example.tqt_quiz.presentation.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.adapters.CourseAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.IReloadableTab;
import com.example.tqt_quiz.presentation.contract_vp.CourseFragmentContract;
import com.example.tqt_quiz.presentation.presenter.CourseFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.presentation.view.activities.ViewCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment implements CourseFragmentContract.IView, IReloadableTab
{
    CourseFragmentContract.IPresenter presenter;
    Button AddCourse;
    private ListView lvCourse;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    private ActivityResultLauncher<Intent> addCourseLauncher;
    private EditText edTx_FindCourse;

    private ActivityResultLauncher<Intent> createCourseLauncher;
    private ActivityResultLauncher<Intent> viewCourseLauncher;

    public CourseFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CourseFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course, container, false);

        lvCourse = view.findViewById(R.id.lv_Course_Course);
        AddCourse = view.findViewById(R.id.btn_Add_Course);
        edTx_FindCourse = view.findViewById(R.id.edt_Find_Course);

        createCourseLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    presenter.showAllMyCourse();
                });

        viewCourseLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    presenter.showAllMyCourse();
                });



        presenter.showAllMyCourse();

        lvCourse.setOnItemClickListener((parent, view1, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(requireContext(), ViewCourse.class);
            intent.putExtra("courseId", selectedCourse.getId());
            viewCourseLauncher.launch(intent);
        });


        AddCourse.setOnClickListener(v -> {
            presenter.onAddCourseClick();
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
        return getActivity().getApplicationContext();
    }

    @Override
    public void showAllList(List<CourseDTO> response) {
        courseList = new ArrayList<>();
        Course course = null;
        for(CourseDTO courseDTO : response)
        {
            course = new Course(courseDTO);
            courseList.add(course);
        }

        courseAdapter = new CourseAdapter(requireContext(), R.layout.item_course, courseList);
        lvCourse.setAdapter(courseAdapter);

    }

    @Override
    public void navigateToLogin() {
        FragmentActivity parent = getActivity();
        Intent i = new Intent(parent.getApplicationContext(), Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showError(String s) {
        Toast.makeText(getTheContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToAddCourse() {
        Intent intent = new Intent(requireContext(), CreateCourse.class);
        createCourseLauncher.launch(intent);
    }

    @Override
    public void onTabReload() {
        presenter.showAllMyCourse();
    }
}