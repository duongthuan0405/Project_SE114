package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.tqt_quiz.presentation.contract_vp.ForgotPasswordContract;
import com.example.tqt_quiz.presentation.contract_vp.ResetPasswordContract;
import com.example.tqt_quiz.presentation.presenter.ForgotPasswordPresenter;
import com.example.tqt_quiz.presentation.presenter.ResetPasswordPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class ResetPassword extends AppCompatActivity implements ResetPasswordContract.IView
{
    Button btnNext;
    EditText edt1;
    EditText edt2;
    EditText edToken;
    TextView tvEmail;
    ResetPasswordContract.IPresenter presenter;
    private String email;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new ResetPasswordPresenter(this);

        btnNext = findViewById(R.id.btn_continue_forgot_password);
        edt1 = findViewById(R.id.edt_email_newPass);
        edt2 = findViewById(R.id.edt_email_renewPass);
        tvEmail = findViewById(R.id.tv_currentEmail);
        edToken = findViewById(R.id.edt_email_token);
        edToken.setInputType(InputType.TYPE_CLASS_TEXT);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        tvEmail.setText(email.toString());

        btnNext.setOnClickListener(v -> {
            String newPassword = edt1.getText().toString();  // New password
            String confirmPassword = edt2.getText().toString();  // Confirm password
            token = edToken.getText().toString();

            // Check if passwords match
            if (newPassword.equals(confirmPassword)) {
                presenter.resetPassword(email, token, newPassword);
            } else {
                Toast.makeText(ResetPassword.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Context getTheContext() {
        return ResetPassword.this.getApplicationContext();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getTheContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishWithOk() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
