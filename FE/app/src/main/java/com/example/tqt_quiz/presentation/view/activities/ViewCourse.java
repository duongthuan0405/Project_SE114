package com.example.tqt_quiz.presentation.view.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.presentation.adapters.MemberAdapter;
import com.example.tqt_quiz.presentation.classes.Member;

import com.example.tqt_quiz.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCourse extends AppCompatActivity {
    ListView lvMembers;
    List<Member> memberList;
    MemberAdapter memberAdapter;

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

        lvMembers = findViewById(R.id.lv_Members_ViewCourse);

        memberList = new ArrayList<>();
        memberList.add(new Member(R.drawable.resource_default, "An", "Nguyễn Văn", "an@gmail.com"));
        memberList.add(new Member(R.drawable.resource_default, "Bình", "Trần Thị", "binh@gmail.com"));
        memberList.add(new Member(R.drawable.resource_default, "Cường", "Lê Văn", "cuong@gmail.com"));

        memberAdapter = new MemberAdapter(this, R.layout.item_member, memberList);
        lvMembers.setAdapter(memberAdapter);

        setListViewHeightBasedOnChildren(lvMembers);

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