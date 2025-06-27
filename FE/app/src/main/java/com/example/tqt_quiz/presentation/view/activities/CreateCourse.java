package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.presentation.contract_vp.CreateCourseContract;
import com.example.tqt_quiz.presentation.presenter.CreateCoursePresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class CreateCourse extends AppCompatActivity implements CreateCourseContract.IView
{

    ImageView Avatar;
    EditText Name, Desc;
    Switch Private;
    Button Create;
    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    CreateCourseContract.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);
        presenter = new CreateCoursePresenter(this);

        //Ánh xạ
        Avatar = findViewById(R.id.img_Avatar_CreateCourse);
        Name = findViewById(R.id.edt_Name_CreateCourse);
        Desc = findViewById(R.id.edt_Description_CreateCourse);
        Private = findViewById(R.id.swt_IsPrivate_CreateCourse);
        Create = findViewById(R.id.btn_Create_CreateCourse);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            Avatar.setImageURI(selectedImageUri);
                        }
                    }
                }
        );

        //Xử lý thao tác thêm ảnh
        Avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        //Xử lý thao tác Create
        Create.setOnClickListener(v -> {
            String name = Name.getText().toString().trim();
            String desc = Desc.getText().toString().trim();
            boolean isPrivate = Private.isChecked();

            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên khóa học", Toast.LENGTH_SHORT).show();
                return;
            }

            String avatarUriStr = selectedImageUri != null ? selectedImageUri.toString() : "";

            //Course course = new Course("", name, desc, isPrivate, "", "Người tạo");

            CourseCreateInfo course = new CourseCreateInfo(name, desc, isPrivate, avatarUriStr);
            presenter.onCreateClick(course);


        });
    }

    @Override
    public Context getTheContext() {
        return CreateCourse.this.getApplicationContext();
    }

    @Override
    public void showSuccess() {
        Toast.makeText(CreateCourse.this, "Thêm khóa học mới thành công", Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishAddCourse() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(CreateCourse.this, Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(CreateCourse.this, s, Toast.LENGTH_LONG).show();
    }
}