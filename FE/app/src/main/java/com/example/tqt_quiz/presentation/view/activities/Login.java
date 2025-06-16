package com.example.tqt_quiz.presentation.view.activities;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    TextView Register, ForgotPw;
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

        //Ánh xạ
        Login = findViewById(R.id.btn_Login_Login);
        Email = findViewById(R.id.edt_Email_ForgotPw);
        Password = findViewById(R.id.edt_Pw_ForgotPw);
        Register = findViewById(R.id.btn_Register_Login);
        ForgotPw = findViewById(R.id.btn_ForgotPw_Login);

        presenter = new LoginPresenter(this);

        //Navigate
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        //Thao tác Login Click
        Login.setOnClickListener(v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            Account account = new Account(email, password);
            presenter.LoginClick(account);
        });

        //Thao tác Register Click
        Register.setOnClickListener(v -> presenter.RegisterClick());

        //Thao tác ForgotPw Click
        ForgotPw.setOnClickListener(v -> presenter.ForgotPasswordClick());

        
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(this, MainHome.class);
        launcher.launch(intent);
    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToRegister() {
        Intent intent = new Intent(Login.this, Register.class);
        launcher.launch(intent);
    }

    @Override
    public void navigateToForgotPassword() {
        Intent intent = new Intent(this, ForgotPassword.class);
        launcher.launch(intent);
    }
}

