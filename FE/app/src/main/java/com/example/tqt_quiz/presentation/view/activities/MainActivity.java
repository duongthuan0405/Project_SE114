package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
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
import com.example.tqt_quiz.presentation.contract_vp.IMainActivityContract;
import com.example.tqt_quiz.presentation.presenter.MainActivityPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

public class MainActivity extends AppCompatActivity implements IMainActivityContract.IView
{
    ActivityResultLauncher<Intent> launcher_Main_Login;
    ActivityResultLauncher<Intent> launcher_Main_MainHome;
    IMainActivityContract.IPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        mainPresenter = new MainActivityPresenter(this);

        launcher_Main_Login = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {

                    }
                }
        );

        launcher_Main_MainHome = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {

                    }
                }
        );


        mainPresenter.onDecideToNavigate();

    }

    @Override
    public Context getTheContext() {
        return MainActivity.this.getApplicationContext();
    }

    @Override
    public void navigateToMainHomeForTeacher() {
        Intent i = new Intent(MainActivity.this, MainHome.class);
        launcher_Main_MainHome.launch(i);
        finish();
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(MainActivity.this, Login.class);
        launcher_Main_Login.launch(i);
        finish();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMainHomeForStudent() {
        showToast("Chưa hỗ trợ student activity");
        navigateToLogin();
    }

    @Override
    public void navigateToMainHomeForAdmin() {
        showToast("Chưa hỗ trọ admin activity");
        navigateToLogin();
    }
}