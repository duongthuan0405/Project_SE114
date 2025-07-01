package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.adapters.MemberAdapter;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.Member;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.contract_vp.ViewCourseContract;
import com.example.tqt_quiz.presentation.interfaces.OnPendingMemberAction;
import com.example.tqt_quiz.presentation.presenter.ViewCoursePresenter;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewCourse extends AppCompatActivity implements ViewCourseContract.IView, OnPendingMemberAction
{

    ImageView avatar;
    TextView name, isPrivate, description, host, tv_CourseId;
    ListView lvMembers;
    List<Member> memberList, pendingList;
    MemberAdapter memberAdapter;
    RadioButton rdbMembers, rdbWaiting;
    Button Deletebutton;
    ViewCourseContract.IPresenter presenter;
    ShapeableImageView logo;
    String courseId;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

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

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new ViewCoursePresenter(this);

        courseId = getIntent().getStringExtra("courseId");

        avatar = findViewById(R.id.img_CourseAvatar_ViewCourseSt);
        name = findViewById(R.id.tv_CourseName_ViewCourseSt);
        isPrivate = findViewById(R.id.tv_IsPrivate_ViewCourseSt);
        description = findViewById(R.id.tv_DescriptionValue_ViewCourseSt);
        host = findViewById(R.id.tv_HostName_ViewCourseSt);
        rdbMembers = findViewById(R.id.rdb_Members_ViewCourse);
        rdbWaiting = findViewById(R.id.rdb_Waiting_ViewCourse);
        lvMembers = findViewById(R.id.lv_Members_ViewCourseSt);
        tv_CourseId = findViewById(R.id.tv_CourseID_ViewCourseSt);
        Deletebutton=findViewById(R.id.btn_DeleteCourse_ViewCourse);

        presenter.showCourseInfo(courseId);

        rdbMembers.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if(isChecked) {
                        presenter.showListMemberOfCourse(courseId);
                    }
                }
        );
        Deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCourse.this);
                builder.setMessage("Bạn có chắn chắn rằng muốn xóa khóa học này không ?");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.DeleteCourse(courseId);
                    }
                });

                builder.create();

                builder.show();
            }
        });
        rdbWaiting.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if(isChecked)
                    {
                        presenter.showListMemberPending(courseId);
                    }

        });

        lvMembers.setOnItemClickListener((parent, view, position, id) -> {

            Member selectedMember =  (Member) lvMembers.getAdapter().getItem(position);
            Intent intent = new Intent(ViewCourse.this, MemberInfo.class);
            intent.putExtra("memberId", selectedMember.getId());
            startActivity(intent);
        });

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            avatar.setImageURI(selectedImageUri);
                            presenter.saveLogo(selectedImageUri, courseId);
                        }
                    }
                }
        );

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                imagePickerLauncher.launch(intent);
            }
        });



        rdbMembers.setChecked(true);
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

    @Override
    public Context getTheContext() {
        return ViewCourse.this.getApplicationContext();
    }

    @Override
    public void showCourseInfo(CourseDTO response) {
        Course course = new Course(response);
        if (course != null) {
            StaticClass.setImage(avatar, course.getAvatar(), R.drawable.resource_default);
            name.setText(course.getName());
            isPrivate.setText("Riêng tư: " + (course.isPrivate() ? "Có" : "Không"));
            description.setText(course.getDescription());
            host.setText("👤Tên giáo viên: " + course.getHostName());
            tv_CourseId.setText("- " + response.getId() + " -");
        }
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(ViewCourse.this.getApplicationContext(), Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(ViewCourse.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showListForAllMember(List<AccountInfo> response) {
        memberList = new ArrayList<>();
        for(AccountInfo accountInfo : response)
        {
            memberList.add(new Member(accountInfo));
        }
        memberAdapter = new MemberAdapter(this, 0, memberList, MemberAdapter.MODE_MEMBER, null);
        lvMembers.setAdapter(memberAdapter);
        setListViewHeightBasedOnChildren(lvMembers);
    }

    @Override
    public void showListMemberPending(List<AccountInfo> response) {
        pendingList = new ArrayList<>();
        for (AccountInfo accountInfo : response)
        {
            pendingList.add(new Member(accountInfo));
        }
        memberAdapter = new MemberAdapter(this, 0, pendingList, MemberAdapter.MODE_PENDING, this);
        lvMembers.setAdapter(memberAdapter);
        setListViewHeightBasedOnChildren(lvMembers);
    }

    @Override
    public void reloadList() {
        presenter.showListMemberPending(courseId);
    }

    @Override
    public void Finish() {
        finish();
    }

    @Override
    public void onAcceptClick(String account_id) {

        presenter.onAcceptAccount(account_id, courseId);
    }

    @Override
    public void onDenyClick(String account_id) {
        presenter.onDenyAccount(account_id, courseId);
    }
}