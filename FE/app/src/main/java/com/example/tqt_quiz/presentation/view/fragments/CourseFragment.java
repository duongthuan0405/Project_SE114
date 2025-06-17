package com.example.tqt_quiz.presentation.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.adapters.CourseAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;
import com.example.tqt_quiz.presentation.view.activities.ViewCourse;

import java.util.ArrayList;
import java.util.List;

public class CourseFragment extends Fragment {

    Button AddCourse;
    private ListView lvCourse;
    private List<Course> courseList;
    private CourseAdapter courseAdapter;

    public CourseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course, container, false);

        lvCourse = view.findViewById(R.id.lv_Course_Course);

        courseList = new ArrayList<>();
        courseList.add(new Course("Lập trình Java", "Mô tả", false, R.drawable.resource_default, "Nguyễn Văn A", ""));
        courseList.add(new Course("Phân tích hệ thống", "Mô tả", true, R.drawable.resource_default, "Trần Thị B", ""));
        courseList.add(new Course("Cơ sở dữ liệu", "Mô tả", false, R.drawable.resource_default, "Lê Văn C", ""));

        courseAdapter = new CourseAdapter(requireContext(), R.layout.item_course, courseList);
        lvCourse.setAdapter(courseAdapter);

        lvCourse.setOnItemClickListener((parent, view1, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(requireContext(), ViewCourse.class);
            intent.putExtra("selected_course", selectedCourse);
            startActivity(intent);
        });

        AddCourse = view.findViewById(R.id.btn_Add_Course);
        AddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CreateCourse.class);
            startActivityForResult(intent, 1);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Course newCourse = (Course) data.getSerializableExtra("new_course");
            if (newCourse != null) {
                courseList.add(newCourse);
                courseAdapter.notifyDataSetChanged();
            }
        }
    }
}