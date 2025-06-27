package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.example.tqt_quiz.presentation.contract_vp.RegisterContract;
import com.example.tqt_quiz.presentation.presenter.RegisterPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class Register extends AppCompatActivity implements RegisterContract.IView {
    private EditText LastName, FirstName, Email, Pw, PwAgain;
    private RadioGroup Type;
    private Button Register;
    private TextView Login;
    private RegisterContract.IPresenter presenter;
    ActivityResultLauncher<Intent> launcher;
    RadioButton teacher, student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);
        //Ánh xạ
        LastName = findViewById(R.id.edt_LastName_Register);
        FirstName = findViewById(R.id.edt_FirstName_Register);
        Email = findViewById(R.id.edt_Email_Register);
        Pw = findViewById(R.id.edt_Pw_Register);
        PwAgain = findViewById(R.id.edt_PwAgain_Register);
        Type = findViewById(R.id.rdg_Type_Register);
        Register = findViewById(R.id.btn_Register_Register);
        Login = findViewById(R.id.btn_Login_Register);
        teacher = findViewById(R.id.rdb_Teacher_Register);
        student = findViewById(R.id.rdb_Student_Register);

        presenter = new RegisterPresenter(this);

        //Navigate
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        //Thao tác Register Click
        Register.setOnClickListener(v -> {
            String type = getSelectedType();
            String lastName = LastName.getText().toString().trim();
            String firstName = FirstName.getText().toString().trim();
            String email = Email.getText().toString().trim();
            String pw = Pw.getText().toString();
            String pwAgain = PwAgain.getText().toString();

            presenter.handleRegister(type, lastName, firstName, email, pw, pwAgain, getSelectedType());
        });

        //Thao tác Login Click
        Login.setOnClickListener(v -> returnLogin());
    }

    private String getSelectedType() {
        int checkedId = Type.getCheckedRadioButtonId();
        if (checkedId == R.id.rdb_Teacher_Register) {
            return StaticClass.AccountTypeId.teacher;
        }
        return StaticClass.AccountTypeId.student;
    }

    @Override
    public void showRegisterSuccess() {
        Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        returnLogin();
    }

    @Override
    public void showRegisterError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getTheContext() {
        return getApplicationContext();
    }

    @Override
    public void returnLogin() {
        com.example.tqt_quiz.presentation.view.activities.Register.this.finish();
    }
}