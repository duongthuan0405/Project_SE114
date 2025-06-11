package com.example.tqt_quiz.presentation.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.data.interactor.AuthInteractorIMP;
import com.example.tqt_quiz.data.repository.Token.TokenManager;
import com.example.tqt_quiz.domain.dto.LoginResponse;
import com.example.tqt_quiz.domain.interactor.AuthInteract;
import com.example.tqt_quiz.presentation.contract_vp.MainActitvityContract;
import com.example.tqt_quiz.presentation.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements MainActitvityContract.IView {
    private MainActitvityContract.IPresenter mainActivityPresenter = null;
    private AuthInteractorIMP interactorIMP=new AuthInteractorIMP();
    private LoginResponse info=null;
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
        TokenManager tokenManager= new TokenManager(getApplicationContext());
        interactorIMP.Login("1", "1", tokenManager, new AuthInteract.AuthCallBack() {
            @Override
            public void onSuccess(LoginResponse response) {
                info=response;
            }

            @Override
            public void onUnAuthorized() {
                Log.d("Login","Unauthorized");
            }

            @Override
            public void FailedByNotResponse() {
                Log.d("Login","CannotSendToServer");
            }
        });

    }

    @Override
    public void showToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }
}