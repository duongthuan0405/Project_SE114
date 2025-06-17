package com.example.tqt_quiz.presentation.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Course;

public class CreateCourse extends AppCompatActivity {

    ImageView Avatar;
    EditText Name, Desc;
    Switch Private;
    Button Create;

    private static final int REQUEST_IMAGE_PICK = 1001;
    private Uri selectedImageUri = null;


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

        //Ánh xạ
        Avatar = findViewById(R.id.img_Avatar_CreateCourse);
        Name = findViewById(R.id.edt_CourseName_CreateCourse);
        Desc = findViewById(R.id.edt_Description_CreateCourse);
        Private = findViewById(R.id.swt_IsPrivate_CreateCourse);
        Create = findViewById(R.id.btn_CreateCourse_CreateCourse);

        //Xử lý thao tác thêm ảnh
        Avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
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

            Course course = new Course(name, desc, isPrivate, "", "Người tạo");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_course", course);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ImageView imgAvatar = findViewById(R.id.img_Avatar_CreateCourse);
                imgAvatar.setImageURI(selectedImageUri);
            }
        }
    }

}