package com.example.tqt_quiz.presentation.view.activities;

import android.accounts.Account;
import android.content.Context;
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
    ActivityResultLauncher<Intent> launcher_Login_Main;
    ActivityResultLauncher<Intent> getLauncher_Login_Register;

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
        launcher_Login_Main = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        getLauncher_Login_Register = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {
                    if(result.getResultCode() == RESULT_OK)
                    {
                        Toast.makeText(Login.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                    }
                }
        );

        //Thao tác Login Click
        Login.setOnClickListener(v -> {
            String email = Email.getText().toString();
            String password = Password.getText().toString();
            presenter.LoginClick(email, password);
        });

        //Thao tác Register Click
        Register.setOnClickListener(v -> presenter.RegisterClick());

        //Thao tác ForgotPw Click
        ForgotPw.setOnClickListener(v -> presenter.ForgotPasswordClick());

        
    }

    @Override
    public void loginSuccess(String roleId) {
        if(roleId.equals("0000000000"))
        {

        }
        else if (roleId.equals("0000000001"))
        {
            Intent i = new Intent(com.example.tqt_quiz.presentation.view.activities.Login.this, MainHome.class);
            launcher_Login_Main.launch(i);
        }
        else if (roleId.equals("0000000002"))
        {

        }

    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(com.example.tqt_quiz.presentation.view.activities.Login.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToRegister() {

    }

    @Override
    public void navigateToForgotPassword() {

    }

    @Override
    public Context getContext() {
        return com.example.tqt_quiz.presentation.view.activities.Login.this.getApplicationContext();
    }
}

