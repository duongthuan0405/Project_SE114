package com.example.tqt_quiz.presentation.view.activities;

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
import com.example.tqt_quiz.presentation.contract_vp.ChangePasswordContract;
import com.example.tqt_quiz.presentation.presenter.ChangePasswordPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePassword extends AppCompatActivity implements ChangePasswordContract.IView
{

    TextView Title;
    private EditText OldPassword, NewPassword, ReNewPassword;
    private Button Save;
    ChangePasswordContract.IPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password); // Dùng lại layout

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new ChangePasswordPresenter(this);

        //Ánh xạ view
        OldPassword = findViewById(R.id.edt_email_token);
        NewPassword = findViewById(R.id.edt_email_newPass);
        ReNewPassword = findViewById(R.id.edt_email_renewPass);
        Save = findViewById(R.id.btn_continue_forgot_password);
        Title = findViewById(R.id.tv_title_forgot_password);

        //Thay đổi nội dung
        ((TextInputLayout)findViewById(R.id.til_email_token)).setHint("Nhập mật khẩu cũ");
        Save.setText("Lưu");

        //Đổi tiêu đề
        Title.setText("Đổi mật khẩu");
        findViewById(R.id.tv_currentEmail).setVisibility(View.GONE);

        //Sự kiện lưu
        Save.setOnClickListener(v -> {
            String oldPass = OldPassword.getText().toString().trim();
            String newPass = NewPassword.getText().toString().trim();
            String reNewPass = ReNewPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || reNewPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(reNewPass)) {
                Toast.makeText(this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.changePassword(newPass, oldPass);
        });
    }

    @Override
    public Context getTheContext() {
        return ChangePassword.this.getApplicationContext();
    }

    @Override
    public void onSuccess() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        presenter.Logout();
        finish();
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(ChangePassword.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getTheContext(), msg, Toast.LENGTH_LONG).show();
    }
}
