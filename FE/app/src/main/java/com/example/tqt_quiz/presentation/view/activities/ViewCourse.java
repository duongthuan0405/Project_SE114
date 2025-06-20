package com.example.tqt_quiz.presentation.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.presentation.adapters.MemberAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.Member;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.util.ArrayList;
import java.util.List;

public class ViewCourse extends AppCompatActivity
{

    ImageView avatar;
    TextView name, isPrivate, description, host;
    ListView lvMembers;
    List<Member> memberList, pendingList;
    MemberAdapter memberAdapter;
    RadioButton rdbMembers, rdbWaiting;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Course course = (Course) getIntent().getSerializableExtra("selected_course");
        String courseId = getIntent().getStringExtra("course_id");

        avatar = findViewById(R.id.img_CourseAvatar_ViewCourse);
        name = findViewById(R.id.tv_CourseName_ViewCourse);
        isPrivate = findViewById(R.id.tv_IsPrivate_ViewCourse);
        description = findViewById(R.id.tv_DescriptionValue_ViewCourse);
        host = findViewById(R.id.tv_HostName_ViewCourse);
        rdbMembers = findViewById(R.id.rdb_Members_ViewCourse);
        rdbWaiting = findViewById(R.id.rdb_Waiting_ViewCourse);

        if (course != null) {
            StaticClass.setImage(avatar, course.getAvatar(), R.drawable.resource_default);
            name.setText(course.getName());
            isPrivate.setText("Riêng tư: " + (course.isPrivate() ? "Có" : "Không"));
            description.setText(course.getDescription());
            host.setText("👤Tên giáo viên: " + course.getHostName());
        }

        lvMembers = findViewById(R.id.lv_Members_ViewCourse);

        memberList = new ArrayList<>();
        memberList.add(new Member("", "An", "Nguyễn Văn", "Học sinh", "an@gmail.com"));
        memberList.add(new Member("", "Bình", "Trần Thị", "Giáo viên", "binh@gmail.com"));
        memberList.add(new Member("", "Cường", "Lê Văn", "Học sinh", "cuong@gmail.com"));

        pendingList = new ArrayList<>();
        pendingList.add(new Member("", "Dũng", "Phan Minh", "Học sinh", "dung@gmail.com"));
        pendingList.add(new Member("", "Hà", "Ngô Thị", "Học sinh", "ha@gmail.com"));

        memberAdapter = new MemberAdapter(this, R.layout.item_member, memberList, MemberAdapter.MODE_MEMBER);
        lvMembers.setAdapter(memberAdapter);

        setListViewHeightBasedOnChildren(lvMembers);

        rdbMembers.setOnClickListener(v -> {
            memberAdapter = new MemberAdapter(this, 0, memberList, MemberAdapter.MODE_MEMBER);
            lvMembers.setAdapter(memberAdapter);
            setListViewHeightBasedOnChildren(lvMembers);
        });

        rdbWaiting.setOnClickListener(v -> {
            memberAdapter = new MemberAdapter(this, 0, pendingList, MemberAdapter.MODE_PENDING);
            lvMembers.setAdapter(memberAdapter);
            setListViewHeightBasedOnChildren(lvMembers);
        });

        lvMembers.setOnItemClickListener((parent, view, position, id) -> {
            Member selectedMember = memberList.get(position);
            Intent intent = new Intent(ViewCourse.this, MemberInfo.class);
            intent.putExtra("selected_member", selectedMember);
            startActivity(intent);
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
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