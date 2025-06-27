package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewQuiz extends AppCompatActivity {

    TextView tvTitle, tvDescription, tvStart, tvDue, tvScore;
    Button btnAction;
    ActivityResultLauncher<Intent> doQuizLauncher, viewResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        tvTitle = findViewById(R.id.tv_Title_ViewQuiz);
        tvDescription = findViewById(R.id.tv_Description_ViewQuiz);
        tvStart = findViewById(R.id.tv_StartTime_ViewQuiz);
        tvDue = findViewById(R.id.tv_DueTime_ViewQuiz);
        tvScore = findViewById(R.id.tv_Score_ViewQuiz);
        btnAction = findViewById(R.id.btn_Action_ViewQuiz);

        String title = getIntent().getStringExtra("quiz_name");
        String description = getIntent().getStringExtra("quiz_description");
        String startTime = getIntent().getStringExtra("quiz_start");
        String dueTime = getIntent().getStringExtra("quiz_due");
        String score = getIntent().getStringExtra("quiz_score");

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvStart.setText("Bắt đầu: " + startTime);
        tvDue.setText("Kết thúc: " + dueTime);
        tvScore.setText("Điểm số: " + (score != null ? score : "--"));

        String status = getStatus(startTime, dueTime);

        if (status.equals("Sắp diễn ra")) {
            btnAction.setVisibility(View.GONE);
        } else if (status.equals("Đang diễn ra")) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Làm bài");
        } else if (status.equals("Đã kết thúc")) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Xem bài làm");
        }

        //Đăng ký Launcher
        doQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newScore = result.getData().getStringExtra("quiz_score");
                        if (newScore != null) {
                            tvScore.setText("Điểm số: " + newScore + " / 10");
                            btnAction.setEnabled(false);
                            btnAction.setText("Đã hoàn thành");
                        }
                    }
                }
        );

        viewResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        //Xử lý Navigate
        btnAction.setOnClickListener(v -> {
            if (status.equals("Đang diễn ra")) {
                Intent intent = new Intent(ViewQuiz.this, DoQuiz.class);
                intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                intent.putExtra("quiz_name", title);
                intent.putExtra("quiz_description", description);
                intent.putExtra("quiz_start", startTime);
                intent.putExtra("quiz_due", dueTime);
                intent.putExtra("course_id", getIntent().getStringExtra("course_id"));

                doQuizLauncher.launch(intent);
            } else if (status.equals("Đã kết thúc")) {
                Intent intent = new Intent(ViewQuiz.this, ViewResult.class);
                intent.putExtra("quiz_id", getIntent().getStringExtra("quiz_id"));
                intent.putExtra("quiz_name", title);
                intent.putExtra("quiz_description", description);
                intent.putExtra("quiz_start", startTime);
                intent.putExtra("quiz_due", dueTime);
                intent.putExtra("course_id", getIntent().getStringExtra("course_id"));
                intent.putExtra("quiz_score", tvScore.getText().toString());

                viewResultLauncher.launch(intent);
            }
        });
    }

    private String getStatus(String startTimeStr, String dueTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date now = new Date();
            Date start = sdf.parse(startTimeStr);
            Date due = sdf.parse(dueTimeStr);

            if (now.before(start)) {
                return "Sắp diễn ra";
            } else if (now.after(due)) {
                return "Đã kết thúc";
            } else {
                return "Đang diễn ra";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "Không xác định";
        }
    }
}