package com.example.tqt_quiz.presentation.view.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.presentation.classes.Member;
import com.example.tqt_quiz.presentation.contract_vp.MemberInfoContract;
import com.example.tqt_quiz.presentation.presenter.MemberInfoPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class MemberInfo extends AppCompatActivity implements MemberInfoContract.IView
{

    ImageView avatar;
    TextView name, type,  email;
    String memberId;
    MemberInfoContract.IPresenter presenter;

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
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new MemberInfoPresenter(this);

        avatar = findViewById(R.id.img_Avatar_MemInfo);
        name = findViewById(R.id.tv_Name_MemInfo);
        type = findViewById(R.id.tv_Type_MemInfo);
        email = findViewById(R.id.tv_Email_MemInfo);


        memberId = getIntent().getStringExtra("memberId");
        presenter.showInfo(memberId);
    }

    @Override
    public Context getTheContext() {
        return MemberInfo.this.getApplicationContext();
    }

    @Override
    public void showInfo(AccountInfo response) {
        if(response != null)
        {

            Member member = new Member(response);
            StaticClass.setImage(avatar, member.getAvatar(), R.drawable.resource_default);
            name.setText(member.getName());
            type.setText(member.getType());
            email.setText(member.getEmail());
        }
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(MemberInfo.this.getApplicationContext(), Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(MemberInfo.this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}