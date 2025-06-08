package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.contract_vp.MainActitvityContract;
import com.example.tqt_quiz.presentation.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActitvityContract.IView {
    private MainActitvityContract.IPresenter mainActivityPresenter = null;
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

        mainActivityPresenter = new MainActivityPresenter(this);
        mainActivityPresenter.onCreateActivity();


        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    public void showToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }
}