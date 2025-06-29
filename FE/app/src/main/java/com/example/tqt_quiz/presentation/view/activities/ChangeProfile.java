package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.staticclass.StaticClass;

public class ChangeProfile extends AppCompatActivity {

    private Uri selectedImageUri = null;
    private ImageView Avatar;
    EditText FirstName, MiddleName, Email;
    Button Save;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

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
        //StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);


//        Avatar = findViewById(R.id.img_ChangeAvt_ChangePf);
//        FirstName = findViewById(R.id.edt_FirstName_ChangePf);
//        MiddleName = findViewById(R.id.edt_MiddleName_ChangePf);
//        Email = findViewById(R.id.edt_Email_ChangePf);
//        Save = findViewById(R.id.btn_Save_ChangePf);

//        imagePickerLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        selectedImageUri = result.getData().getData();
//                        if (selectedImageUri != null) {
//                            Avatar.setImageURI(selectedImageUri);
//                        }
//                    }
//                }
//        );
//
//        Avatar.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            imagePickerLauncher.launch(intent);
//        });
//
//        Save.setOnClickListener(v -> {
//            String firstName = FirstName.getText().toString().trim();
//            String middleName = MiddleName.getText().toString().trim();
//            String email = Email.getText().toString().trim();
//
//            if (firstName.isEmpty() || middleName.isEmpty()) {
//                Toast.makeText(ChangeProfile.this, "Vui lòng nhập đầy đủ họ tên", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Gửi dữ liệu về lại Fragment
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("first_name", firstName);
//            resultIntent.putExtra("middle_name", middleName);
//            resultIntent.putExtra("email", email);
//
//            if (selectedImageUri != null) {
//                resultIntent.setData(selectedImageUri); // avatar URI
//            }
//
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        });
    }
}