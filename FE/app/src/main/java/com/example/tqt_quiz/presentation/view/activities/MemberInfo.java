package com.example.tqt_quiz.presentation.view.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Member;
import com.example.tqt_quiz.staticclass.StaticClass;

public class MemberInfo extends AppCompatActivity {

    ImageView avatar;
    TextView name, type,  email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_member_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        avatar = findViewById(R.id.img_Avatar_MemInfo);
        name = findViewById(R.id.tv_Name_MemInfo);
        type = findViewById(R.id.tv_Type_MemInfo);
        email = findViewById(R.id.tv_Email_MemInfo);

        Member member = (Member) getIntent().getSerializableExtra("selected_member");

        if (member != null) {
            StaticClass.setImage(avatar, member.getAvatar(), R.drawable.resource_default);
            name.setText(member.getName());
            type.setText(member.getType());
            email.setText(member.getEmail());
        }
    }
}