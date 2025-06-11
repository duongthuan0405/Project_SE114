package com.example.tqt_quiz.presentation.view.activities;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;
import com.example.tqt_quiz.presentation.presenter.LoginPresenter;

public class Login extends AppCompatActivity implements LoginContract.IView {
    Button Login;
    EditText Email, Password;
    LoginPresenter presenter;
    ActivityResultLauncher<Intent> launcher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Login = findViewById(R.id.btn_Login_Login);
        Email = findViewById(R.id.edt_Email_Login);
        Password = findViewById(R.id.edt_Pw_Login);

        presenter = new LoginPresenter(this);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "Trở về từ MainHome", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        Login.setOnClickListener(v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            Account account = new Account(email, password);
            presenter.LoginClick(account);
        });
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainHome.class);
        launcher.launch(intent);
    }

    @Override
    public void loginFailed() {
        Toast.makeText(this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
    }
}

