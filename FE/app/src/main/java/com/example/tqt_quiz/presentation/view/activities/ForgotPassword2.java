package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.contract_vp.ForgotPasswordContract;
import com.example.tqt_quiz.presentation.presenter.ForgotPasswordPresenter;

public class ForgotPassword2 extends AppCompatActivity implements ForgotPasswordContract.IView
{
    Button btnNext;
    EditText editTextMail;
    ForgotPasswordContract.IPresenter presenter;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        presenter = new ForgotPasswordPresenter(this);

        btnNext = findViewById(R.id.btn_continue_forgot_password);
        editTextMail = findViewById(R.id.edt_email_forgot_password);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickContinue(editTextMail.getText().toString());
            }
        });

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK)
                        {
                            Intent i = new Intent();
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                }
        );

    }

    @Override
    public Context getTheContext() {
        return ForgotPassword2.this.getApplicationContext();
    }

    @Override
    public void navigateToResetPassword() {
        Intent i = new Intent(ForgotPassword2.this, ResetPassword.class);
        i.putExtra("email", editTextMail.getText().toString());
        launcher.launch(i);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getTheContext(), msg, Toast.LENGTH_LONG).show();
    }
}