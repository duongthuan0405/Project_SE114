package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.APIService.GetQuizScoreService;
import com.example.tqt_quiz.domain.dto.AccountWithScore;
import com.example.tqt_quiz.domain.dto.QuizScoreBoardDTO;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewScore extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvCourseId, tvStartTime, tvDueTime;
    private LinearLayout llScoreList;
    private GetQuizScoreService quizScoreService;
    private String quizId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_score);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root_view_score), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ
        tvTitle = findViewById(R.id.tv_Title_ViewScore);
        tvDescription = findViewById(R.id.tv_Description_ViewScore);
        tvCourseId = findViewById(R.id.tv_CourseId_ViewScore);
        tvStartTime = findViewById(R.id.tv_StartTime_ViewScore);
        tvDueTime = findViewById(R.id.tv_DueTime_ViewScore);
        llScoreList = findViewById(R.id.ll_ScoreList_ViewScore);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        quizId = intent.getStringExtra("quizId");
        String quizName = intent.getStringExtra("quizName");
        String quizDescription = intent.getStringExtra("quizDescription");
        String courseId = intent.getStringExtra("courseId");
        String startTime = intent.getStringExtra("startTime");
        String dueTime = intent.getStringExtra("dueTime");

        // Set dữ liệu
        tvTitle.setText(quizName != null ? quizName : "Không có tiêu đề");
        tvDescription.setText(quizDescription != null ? quizDescription : "Không có mô tả");
        tvCourseId.setText("Course ID: " + (courseId != null ? courseId : "--"));
        tvStartTime.setText("Bắt đầu: " + (startTime != null ? startTime : "--"));
        tvDueTime.setText("Kết thúc: " + (dueTime != null ? dueTime : "--"));

        // Gọi API để lấy danh sách điểm số thực tế
        fetchQuizScores();
    }

    private void fetchQuizScores() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticClass.BareUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*
        quizScoreService = retrofit.create(GetQuizScoreService.class);
        quizScoreService.GetQuizScore(quizId).enqueue(new Callback<QuizScoreBoardDTO>() {
            @Override
            public void onResponse(Call<QuizScoreBoardDTO> call, Response<QuizScoreBoardDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (AccountWithScore score : response.body().getScores()) {
                        addScoreItem(score);
                    }
                } else {
                    Toast.makeText(ViewScore.this, "Lỗi khi tải điểm số", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizScoreBoardDTO> call, Throwable t) {
                Toast.makeText(ViewScore.this, "Không thể kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    private void addScoreItem(AccountWithScore score) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View scoreItem = inflater.inflate(R.layout.item_score, llScoreList, false);

        TextView tvName = scoreItem.findViewById(R.id.tv_Name_ScoreItem);
        TextView tvScore = scoreItem.findViewById(R.id.tv_Score_ScoreItem);
        ShapeableImageView imgAvatar = scoreItem.findViewById(R.id.img_Avatar_ScoreItem);

        tvName.setText(score.getAccount().getFullName());
        tvScore.setText(score.isSubmitted() ?
                String.format("%d / %d", score.getTotalCorrectAnswer(), score.getTotalQuestions()) :
                "-- / --");

        StaticClass.setImage(imgAvatar, score.getAccount().getAvatar(), R.drawable.resource_default);

        llScoreList.addView(scoreItem);
    }
}
