package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.contract_vp.ViewScoreContract;
import com.example.tqt_quiz.presentation.presenter.ViewScorePresenter;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewScore extends AppCompatActivity implements ViewScoreContract.IView
{

    private TextView tvTitle, tvDescription, tvCourseId, tvStartTime, tvDueTime;
    private LinearLayout llScoreList;
    private GetQuizScoreService quizScoreService;
    private String quizId;
    ViewScoreContract.IPresenter presenter;

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

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new ViewScorePresenter(this);
        // Ánh xạ
        tvTitle = findViewById(R.id.tv_Title_ViewScore);
        tvDescription = findViewById(R.id.tv_Description_ViewScore);
        tvCourseId = findViewById(R.id.tv_CourseId_ViewScore);
        tvStartTime = findViewById(R.id.tv_StartTime_ViewScore);
        tvDueTime = findViewById(R.id.tv_DueTime_ViewScore);
        llScoreList = findViewById(R.id.ll_ScoreList_ViewScore);
        Intent i = getIntent();
        quizId = i.getStringExtra("quizId");
        presenter.quizInfo(quizId);
        presenter.getResultForTeacherView(quizId);

    }



    @Override
    public Context getTheContext() {
        return ViewScore.this.getApplicationContext();
    }

    @Override
    public void onSuccessGetQuizInfo(QuizDTO response) {
        tvTitle.setText(response.getName());
        tvDescription.setText(response.getDescription());
        tvCourseId.setText(String.format("Khóa học: %s (%s)", response.getCourseName(), response.getCourseId()));
        tvStartTime.setText("Bắt đầu: " + response.getStartTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        tvDueTime.setText("Kết thúc: " + response.getDueTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(ViewScore.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(getTheContext(), s, Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccessShowResult(List<AccountWithScore> response) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (AccountWithScore a : response)
        {
            View scoreItem = inflater.inflate(R.layout.item_score, llScoreList, false);

            TextView tvName = scoreItem.findViewById(R.id.tv_Name_ScoreItem);
            TextView tvScore = scoreItem.findViewById(R.id.tv_Score_ScoreItem);
            ShapeableImageView imgAvatar = scoreItem.findViewById(R.id.img_Avatar_ScoreItem);
            TextView tvCorrectAnswer = scoreItem.findViewById(R.id.tv_Correct_MemInfo_ScoreItem);
            TextView tvTimeToDo = scoreItem.findViewById(R.id.tv_TimeToDo_ScoreItem);

            tvName.setText(a.getAccount().getFullName());
            float score = (float)a.getTotalCorrectAnswer() / a.getTotalQuestions() * 10;
            tvScore.setText(String.format("%.1f", score));
            StaticClass.setImage(imgAvatar, a.getAccount().getAvatar(), R.drawable.resource_default);
            llScoreList.addView(scoreItem);
            tvCorrectAnswer.setText(String.format("Kết quả: %d / %d", a.getTotalCorrectAnswer(), a.getTotalQuestions()));
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


            if(a.getStartAt() == null)
            {
                tvTimeToDo.setText("Thời gian: Chưa tham gia");
                tvTimeToDo.setTextColor(Color.parseColor("#F44336"));
            }
            else if(a.getFinishAt() == null)
            {
                tvTimeToDo.setText("Thời gian: Chưa nộp bài");
                tvTimeToDo.setTextColor(Color.parseColor("#F28705"));
            }
            else
            {
                Duration d = Duration.between(a.getStartAt(), a.getFinishAt());
                long totalSecs = d.getSeconds();
                long hours = totalSecs / 3600;
                long mins = (totalSecs - hours * 3600) / 60;
                long sec = totalSecs - hours * 3600 - mins * 60;
                tvTimeToDo.setText(String.format("Thời gian: %d giờ %d phút %d giây", hours, mins, sec ));
            }

        }
    }

}
