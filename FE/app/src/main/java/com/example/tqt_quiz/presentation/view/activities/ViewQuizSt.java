package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.QuizWithScoreDTO;
import com.example.tqt_quiz.presentation.contract_vp.ViewQuizStContract;
import com.example.tqt_quiz.presentation.presenter.ViewQuizStPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class ViewQuizSt extends AppCompatActivity implements ViewQuizStContract.IView
{

    TextView tvTitle, tvDescription, tvStart, tvDue, tvCorrect, tvScore;
    Button btnAction;
    ActivityResultLauncher<Intent> doQuizLauncher, viewResultLauncher;
    ViewQuizStContract.IPresenter presenter;
    String quizId = "";
    private QuizWithScoreDTO quizWithScoreDTO;

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
        tvCorrect = findViewById(R.id.tv_NumberOfCorrect_ViewQuiz);
        btnAction = findViewById(R.id.btn_Action_ViewQuiz);
        tvScore = findViewById(R.id.tv_Score_ViewQuiz);

        presenter = new ViewQuizStPresenter(this);

        Intent i = getIntent();
        quizId = i.getStringExtra("quizId");

        doQuizLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String newScore = result.getData().getStringExtra("quiz_score");
                        if (newScore != null) {
                            tvCorrect.setText("Điểm số: " + newScore + " / 10");
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

        presenter.getQuizWithScore(quizId);
        btnAction.setOnClickListener(v -> {
            String status = getStatus(quizWithScoreDTO.getQuiz().getStartTime(), quizWithScoreDTO.getQuiz().getDueTime());
            if (status.equals(StaticClass.StateOfQuiz.NOW)) {
                Intent intent = new Intent(ViewQuizSt.this, DoQuiz.class);
                intent.putExtra("quizId", quizId);
                intent.putExtra("dueTime", quizWithScoreDTO.getQuiz().getDueTime());
                doQuizLauncher.launch(intent);

            } else if (status.equals(StaticClass.StateOfQuiz.END)) {
                Intent intent = new Intent(ViewQuizSt.this, ViewResult.class);
                intent.putExtra("quizId", quizId);
                intent.putExtra("totalCorrect", quizWithScoreDTO.getTotalCorrectAnswer());
                intent.putExtra("totalQuestion", quizWithScoreDTO.getTotalQuestion());
                viewResultLauncher.launch(intent);
            }
        });
    }

    private String getStatus(LocalDateTime startTimeStr, LocalDateTime dueTimeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(StaticClass.DateTimeFormat, Locale.getDefault());
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = startTimeStr;
            LocalDateTime due = dueTimeStr;

            if (now.isBefore(start)) {
                return StaticClass.StateOfQuiz.SOON;
            } else if (now.isAfter(due)) {
                return StaticClass.StateOfQuiz.END;
            } else {
                return StaticClass.StateOfQuiz.NOW;
            }

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return "Không xác định";
        }
    }

    @Override
    public Context getTheContext() {
        return ViewQuizSt.this.getApplicationContext();
    }

    @Override
    public void handleOnResponse(QuizWithScoreDTO response) {
        quizWithScoreDTO = response;
        tvTitle.setText(response.getQuiz().getName());
        tvDescription.setText(response.getQuiz().getDescription());
        tvStart.setText("Bắt đầu: " + response.getQuiz().getStartTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        tvDue.setText("Kết thúc: " + response.getQuiz().getDueTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));

        String status = getStatus(quizWithScoreDTO.getQuiz().getStartTime(), quizWithScoreDTO.getQuiz().getDueTime());

        if (status.equals(StaticClass.StateOfQuiz.SOON)) {
            btnAction.setVisibility(View.GONE);
            tvCorrect.setText("Kết quả: -- / --");
            tvScore.setText("Điểm số: --");
        }
        else if (status.equals(StaticClass.StateOfQuiz.NOW)) {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Làm bài");
            tvCorrect.setText("Kết quả: -- / --");
            tvScore.setText("Điểm số: --");

            if(response.isSubmitted())
            {
                btnAction.setEnabled(false);
                btnAction.setClickable(false);
                btnAction.setText("Đã nộp bài");
            }
        } else if (status.equals(StaticClass.StateOfQuiz.END))
        {
            btnAction.setVisibility(View.VISIBLE);
            btnAction.setText("Xem bài làm");
            tvCorrect.setText(String.format("Kết quả: %d / %d", quizWithScoreDTO.getTotalCorrectAnswer(), quizWithScoreDTO.getTotalQuestion()));
            float score = (float)quizWithScoreDTO.getTotalCorrectAnswer() / quizWithScoreDTO.getTotalQuestion() * 10;
            tvScore.setText(String.format("Điểm số: %.1f", score));
            if(score < 5f)
            {
                tvScore.setBackgroundResource(R.drawable.bg_status_ended);
            }
            else if(score < 8f)
            {
                tvScore.setBackgroundResource(R.drawable.bg_status_benotpublished);
            }
            else
            {
                tvScore.setBackgroundResource(R.drawable.btn_rectangle_green);
            }
        }
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(ViewQuizSt.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(getTheContext(), s, Toast.LENGTH_LONG).show();
    }
}