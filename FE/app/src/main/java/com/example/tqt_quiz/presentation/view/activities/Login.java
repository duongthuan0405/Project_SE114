package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.presentation.contract_vp.LoginContract;
import com.example.tqt_quiz.presentation.presenter.LoginPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class Login extends AppCompatActivity implements LoginContract.IView {
    Button btn_Login;
    EditText edt_Email, edt_Password;
    TextView tv_Register, tv_ForgotPw;
    LoginPresenter presenter;
    ActivityResultLauncher<Intent> launcher_Login_Main;
    ActivityResultLauncher<Intent> getLauncher_Login_Register;

    ActivityResultLauncher<Intent> launcher_Login_ForgotPass;

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

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        //Ánh xạ
        btn_Login = findViewById(R.id.btn_Login_Login);
        edt_Email = findViewById(R.id.edt_Email_Login);
        edt_Password = findViewById(R.id.edt_Pw_Login);
        tv_Register = findViewById(R.id.btn_Register_Login);
        tv_ForgotPw = findViewById(R.id.btn_ForgotPw_Login);

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

        launcher_Login_ForgotPass = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result ->
                {

                }
        );

        btn_Login.setOnClickListener(v -> {
            String email = edt_Email.getText().toString();
            String password = edt_Password.getText().toString();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                showLoginError("Email không đúng định dạng");
                return;
            }
            presenter.LoginClick(email, password);
        });

        tv_Register.setOnClickListener(v -> presenter.RegisterClick());

        tv_ForgotPw.setOnClickListener(v -> presenter.ForgotPasswordClick());

        
    }

    @Override
    public void loginSuccess(LoginResponse loginResponse) {
        Intent i = new Intent(com.example.tqt_quiz.presentation.view.activities.Login.this, MainHome.class);
        launcher_Login_Main.launch(i);
        finish();

    }

    @Override
    public void showLoginError(String message) {
        Toast.makeText(com.example.tqt_quiz.presentation.view.activities.Login.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToRegister() {
        Intent i = new Intent(Login.this, Register.class);
        getLauncher_Login_Register.launch(i);
    }

    @Override
    public void navigateToForgotPassword()
    {
        Intent i = new Intent(Login.this, ForgotPassword.class);
        launcher_Login_ForgotPass.launch(i);
    }

    @Override
    public Context getContext() {
        return com.example.tqt_quiz.presentation.view.activities.Login.this.getApplicationContext();
    }
}

