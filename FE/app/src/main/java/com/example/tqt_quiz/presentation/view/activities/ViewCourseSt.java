package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.adapters.MemberAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.Member;
import com.example.tqt_quiz.presentation.contract_vp.ViewCourseContract;
import com.example.tqt_quiz.presentation.presenter.ViewCoursePresenter;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewCourseSt extends AppCompatActivity {

    TextView name, isPrivate, description, host, tv_CourseId;
    ListView lvMembers;
    List<Member> memberList;
    MemberAdapter memberAdapter;
    ShapeableImageView avatar;
    String courseId;

    ViewCourseContract.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_course_st);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        // Ánh xạ
        avatar = findViewById(R.id.img_CourseAvatar_ViewCourseSt);
        name = findViewById(R.id.tv_CourseName_ViewCourseSt);
        isPrivate = findViewById(R.id.tv_IsPrivate_ViewCourseSt);
        description = findViewById(R.id.tv_DescriptionValue_ViewCourseSt);
        host = findViewById(R.id.tv_HostName_ViewCourseSt);
        tv_CourseId = findViewById(R.id.tv_CourseID_ViewCourseSt);
        lvMembers = findViewById(R.id.lv_Members_ViewCourseSt);

        // Khởi tạo presenter
        presenter = new ViewCoursePresenter(new ViewCourseContract.IView() {
            @Override
            public Context getTheContext() {
                return ViewCourseSt.this.getApplicationContext();
            }

            @Override
            public void showCourseInfo(CourseDTO response) {
                Course course = new Course(response);
                StaticClass.setImage(avatar, course.getAvatar(), R.drawable.resource_default);
                name.setText(course.getName());
                isPrivate.setText("Riêng tư: " + (course.isPrivate() ? "Có" : "Không"));
                description.setText(course.getDescription());
                host.setText("👤Tên giáo viên: " + course.getHostName());
                tv_CourseId.setText("- " + course.getId() + " -");
            }

            @Override
            public void navigateToLogin() {
                startActivity(new Intent(ViewCourseSt.this, Login.class));
                finish();
            }

            @Override
            public void showToast(String msg) {
                Toast.makeText(ViewCourseSt.this, msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void showListForAllMember(List<AccountInfo> response) {
                memberList = new ArrayList<>();
                for (AccountInfo info : response) {
                    memberList.add(new Member(info));
                }
                memberAdapter = new MemberAdapter(ViewCourseSt.this, 0, memberList, MemberAdapter.MODE_MEMBER, null);
                lvMembers.setAdapter(memberAdapter);
                setListViewHeightBasedOnChildren(lvMembers);
            }

            @Override
            public void showListMemberPending(List<AccountInfo> response) { }

            @Override
            public void reloadList() { }

            @Override
            public void Finish() { finish(); }
        });

        // Lấy ID và load dữ liệu
        courseId = getIntent().getStringExtra("courseId");
        presenter.showCourseInfo(courseId);
        presenter.showListMemberOfCourse(courseId);

        lvMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member selectedMember = (Member) lvMembers.getAdapter().getItem(position);
            Intent intent = new Intent(ViewCourseSt.this, MemberInfo.class);
            intent.putExtra("memberId", selectedMember.getId());
            startActivity(intent);
        });
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        MemberAdapter adapter = (MemberAdapter) listView.getAdapter();
        if (adapter == null) return;

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
