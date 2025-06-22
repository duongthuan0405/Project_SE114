package com.example.tqt_quiz.presentation.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.tqt_quiz.presentation.contract_vp.ICourseFragmentContract;
import com.example.tqt_quiz.presentation.presenter.CourseFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.presentation.view.activities.ViewCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment implements ICourseFragmentContract.IView
{
    ICourseFragmentContract.IPresenter presenter;
    Button AddCourse;
    private ListView lvCourse;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;
    private ActivityResultLauncher<Intent> addCourseLauncher;
    private EditText edTx_FindCourse;

    public CourseFragment() {

    }

    private final ActivityResultLauncher<Intent> createCourseLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    presenter.showAllMyCourse();
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = new CourseFragmentPresenter(this);
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        lvCourse = view.findViewById(R.id.lv_Course_Course);
        AddCourse = view.findViewById(R.id.btn_Add_Course);
        edTx_FindCourse = view.findViewById(R.id.edt_Find_Course);
/*
        courseList = new ArrayList<>();
        courseList.add(new Course("Lập trình Java", "Mô tả", false, "", "Nguyễn Văn A"));
        courseList.add(new Course("Phân tích hệ thống", "Mô tả", true, "", "Trần Thị B"));
        courseList.add(new Course("Cơ sở dữ liệu", "Mô tả", false, "", "Lê Văn C"));

 */
        presenter.showAllMyCourse();

        lvCourse.setOnItemClickListener((parent, view1, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(requireContext(), ViewCourse.class);
            intent.putExtra("course_id", selectedCourse.getId());
            intent.putExtra("selected_course", selectedCourse);
            startActivity(intent);
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
                ((CourseAdapter)lvCourse.getAdapter()).filtCourse(s.toString());
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
}