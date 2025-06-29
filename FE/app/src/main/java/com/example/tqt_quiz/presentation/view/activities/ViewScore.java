package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewScore extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvCourseId, tvStartTime, tvDueTime;
    private LinearLayout llScoreList;

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

        // Giả lập danh sách điểm học sinh (sau này bạn thay bằng dữ liệu thật từ API)
        List<StudentScore> studentScores = getDummyScores();

        // Hiển thị danh sách
        LayoutInflater inflater = LayoutInflater.from(this);
        for (StudentScore score : studentScores) {
            View scoreItem = inflater.inflate(R.layout.item_score, llScoreList, false);

            TextView tvName = scoreItem.findViewById(R.id.tv_Name_ScoreItem);
            TextView tvScore = scoreItem.findViewById(R.id.tv_Score_ScoreItem);
            ShapeableImageView imgAvatar = scoreItem.findViewById(R.id.img_Avatar_ScoreItem);

            tvName.setText(score.name);
            tvScore.setText(String.valueOf(score.score));
            imgAvatar.setImageResource(R.drawable.resource_default); // bạn có thể dùng Glide/Picasso nếu có URL

            llScoreList.addView(scoreItem);
        }
    }

    // Dữ liệu mẫu
    private List<StudentScore> getDummyScores() {
        List<StudentScore> list = new ArrayList<>();
        list.add(new StudentScore("Nguyễn Văn A", 9.5));
        list.add(new StudentScore("Trần Thị B", 8.0));
        list.add(new StudentScore("Lê Văn C", 7.25));
        list.add(new StudentScore("Phạm Thị D", 10.0));
        return list;
    }

    // Lớp đơn giản đại diện cho điểm số
    private static class StudentScore {
        String name;
        double score;

        StudentScore(String name, double score) {
            this.name = name;
            this.score = score;
        }
    }
}
