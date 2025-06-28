package com.example.tqt_quiz.presentation.view.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;

public class ChangePassword extends AppCompatActivity {

    TextView Title;
    private EditText OldPassword, NewPassword, ReNewPassword;
    private Button Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password); // Dùng lại layout

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Ánh xạ view
        OldPassword = findViewById(R.id.edt_email_token);
        NewPassword = findViewById(R.id.edt_email_newPass);
        ReNewPassword = findViewById(R.id.edt_email_renewPass);
        Save = findViewById(R.id.btn_continue_forgot_password);
        Title = findViewById(R.id.tv_title_forgot_password);

        //Thay đổi nội dung
        OldPassword.setHint("Nhập mật khẩu cũ");
        Save.setText("Lưu");

        //Đổi tiêu đề
        Title.setText("Đổi mật khẩu");

        //Sự kiện lưu
        Save.setOnClickListener(v -> {
            String oldPass = OldPassword.getText().toString().trim();
            String newPass = NewPassword.getText().toString().trim();
            String reNewPass = ReNewPassword.getText().toString().trim();

            if (oldPass.isEmpty() || newPass.isEmpty() || reNewPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(reNewPass)) {
                Toast.makeText(this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Đổi mật khẩu thành công (demo)", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}
