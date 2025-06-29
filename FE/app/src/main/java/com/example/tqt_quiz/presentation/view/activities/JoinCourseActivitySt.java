package com.example.tqt_quiz.presentation.view.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.presentation.contract_vp.JoinCourseSTContract;
import com.example.tqt_quiz.presentation.presenter.JoinCourseStPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

public class JoinCourseActivitySt extends AppCompatActivity implements JoinCourseSTContract.IView {

    EditText FindText=null;
    TextView CourseName=null;
    ShapeableImageView CourseImage=null;
    TextView CourseId=null;
    TextView Private=null;
    TextView Description=null;
    TextView TeacherName=null;
    Button JoinButton=null;
    Button FindButton=null;
    JoinCourseStPresenter presenter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_course_st);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);
        BindingUI();
        DisableJoin();
        presenter=new JoinCourseStPresenter(this);
        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.JoinCourse(CourseId.getText().toString());
            }
        });
        FindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.SearchCourse(FindText.getText().toString());
            }
        });
    }

    @Override
    public Context GetContext() {
        return this;
    }

    @Override
    public void ShowSearchedCourse(CourseDTO Course) {
        CourseName.setText(Course.getName());
        CourseId.setText(Course.getId());
        Private.setText("Riêng tư: "+(Course.isPrivate()?"Có":"Không"));
        Description.setText(Course.getDescription());
        TeacherName.setText("Giáo viên: "+Course.getHostName());
        StaticClass.setImage(CourseImage,Course.getAvatar(),R.drawable.resource_default);
    }

    @Override
    public void JoinCourse(String Id) {
        presenter.JoinCourse(Id);
    }

    @Override
    public void BindingUI() {
        FindText=findViewById(R.id.edt_Find_CourseSt);
        CourseName=findViewById(R.id.tv_CourseName_ViewCourseSt);
        CourseImage=findViewById(R.id.img_CourseAvatar_ViewCourseSt);
        CourseId=findViewById(R.id.tv_CourseID_ViewCourseSt);
        Private=findViewById(R.id.tv_IsPrivate_ViewCourseSt);
        Description=findViewById(R.id.tv_DescriptionValue_ViewCourseSt);
        TeacherName=findViewById(R.id.tv_HostName_ViewCourseSt);
        JoinButton=findViewById(R.id.btn_Join);
        FindButton=findViewById(R.id.btn_Find);
    }

    @Override
    public void ShowToast(String msg) {
        Toast.makeText(this,msg,LENGTH_SHORT).show();
    }

    @Override
    public void DisableJoin() {
        JoinButton.setEnabled(false);
        CourseName.setText("");
        CourseId.setText("");
        Private.setText("Riêng tư: ");
        Description.setText("");
        TeacherName.setText("Giáo viên: ");
        StaticClass.setImage(CourseImage,null,R.drawable.resource_default);
    }

    @Override
    public void EnableJoin() {
        JoinButton.setEnabled(true);
    }

    @Override
    public void navigateToLogin() {
        Intent i= new Intent(JoinCourseActivitySt.this.getApplicationContext(), Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}