package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.UploadResponse;
import com.example.tqt_quiz.presentation.contract_vp.ChangeProfileContract;
import com.example.tqt_quiz.presentation.presenter.ChangeProfilePresenter;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

public class ChangeProfile extends AppCompatActivity implements ChangeProfileContract.IView
{

    Button btn_Save;
    EditText et_FirstName, et_LastName;
    ShapeableImageView iv_Avatar;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    ChangeProfileContract.IPresenter presenter;
    AccountInfo oldProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new ChangeProfilePresenter(this);

        btn_Save = findViewById(R.id.btn_Save_ChangePf);
        et_FirstName = findViewById(R.id.edt_FirstName_ChangePf);
        et_LastName = findViewById(R.id.edt_MiddleName_ChangePf);
        iv_Avatar = findViewById(R.id.img_ChangeAvt_ChangePf);

        oldProfile = (AccountInfo) getIntent().getSerializableExtra("oldProfile");

        showOldProfile();

        imagePickerLauncher = imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            presenter.saveAvatar(selectedImageUri);
                        }
                    }
                }
        );

        //StaticClass.setImage(iv_Avatar, );

        iv_Avatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btn_Save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.onChangeProfile(et_FirstName.getText().toString(), et_LastName.getText().toString());
                    }
                }
        );


    }

    private void showOldProfile() {
        this.et_LastName.setText(oldProfile.getLastMiddleName());
        this.et_FirstName.setText(oldProfile.getFirstName());
        StaticClass.setImage(iv_Avatar, oldProfile.getAvatar(), R.drawable.resource_default);
    }

    @Override
    public Context getTheContext() {
        return ChangeProfile.this.getApplicationContext();
    }

    @Override
    public void onResponse(UploadResponse response) {
        StaticClass.setImage(iv_Avatar, response.getUrl(), R.drawable.resource_default);
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(ChangeProfile.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(getTheContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishOK() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}