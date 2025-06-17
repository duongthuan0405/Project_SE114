package com.example.tqt_quiz.domain.repository;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.ArrayList;
import java.util.List;

public class FakeCourseRepository {

    public static List<Course> getDummyCourses() {
        List<Course> courses = new ArrayList<>();

        courses.add(new Course("Lập trình Android", "Nguyễn Văn A", R.drawable.resource_default));
        courses.add(new Course("Java cơ bản", "Trần Thị B", R.drawable.resource_default));
        courses.add(new Course("Thiết kế UI/UX", "Lê Văn C", R.drawable.resource_default));
        courses.add(new Course("Kỹ thuật phần mềm", "Phạm Thị D", R.drawable.resource_default));
        courses.add(new Course("Lập trình Web", "Hoàng Văn E", R.drawable.resource_default));

        return courses;
    }

}
