package com.example.tqt_quiz.presentation.view.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.QuestionViewHolder;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.time.Duration;
import java.time.LocalDateTime;
import com.example.tqt_quiz.presentation.contract_vp.DoQuizContract;
import com.example.tqt_quiz.presentation.presenter.DoQuizPresenter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoQuiz extends AppCompatActivity implements DoQuizContract.IView {

    private TextView Title, Description, StartTime, DueTime, CourseId, Timer;
    private LinearLayout QuestionList;
    private Button Finish;
    private DoQuizContract.IPresenter presenter;
    private AttemptQuizDTO currentattemptinfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_do_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root_do_quiz), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        presenter = new DoQuizPresenter(this);

        // Ánh xạ view
        Title = findViewById(R.id.tv_Title_DoQuiz);
        Description = findViewById(R.id.tv_Description_DoQuiz);
        StartTime = findViewById(R.id.tv_StartTime_DoQuiz);
        DueTime = findViewById(R.id.tv_DueTime_DoQuiz);
        CourseId = findViewById(R.id.tv_CourseId_DoQuiz);
        QuestionList = findViewById(R.id.ll_QuestionList_DoQuiz);
        Finish = findViewById(R.id.btn_Finish_DoQuiz);
        Timer = findViewById(R.id.tv_Timer_DoQuiz);


        Intent intent = getIntent();
        String quizId=intent.getStringExtra("quizId");
        LocalDateTime dueTime = (LocalDateTime) getIntent().getExtras().get("dueTime");

        presenter.StartAttempt(quizId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat);
        LocalDateTime now = LocalDateTime.now();

        long millisUntilDue = Duration.between(now, dueTime).toMillis();
        if (millisUntilDue > 0) {
            startCountdown(millisUntilDue);
        }
        else
        {

        }

        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoQuiz.this);
                builder.setMessage("Bạn có chắn chắn rằng muốn nộp bài");
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.submit(currentattemptinfo.getQuizId());
                    }
                });

                builder.create();

                builder.show();
            }
        });
    }

    @Override
    public Context GetTheContext() {
        return this;
    }

    @Override
    public void ShowQuiz(List<QuestionDTO> questionlist) {
        LayoutInflater inflater = LayoutInflater.from(this);
        Question q = null;
        for (QuestionDTO question : questionlist) {
            q = new Question(question);
            View questionView = inflater.inflate(R.layout.item_question, QuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, false);
            questionView.setTag(viewHolder);

            viewHolder.setDataWithSelectedAnswer(q);

            viewHolder.getRoot().findViewById(R.id.btn_Add_QuestionItem).setVisibility(View.GONE);
            viewHolder.getRoot().findViewById(R.id.btn_Delete_QuestionItem).setVisibility(View.GONE);
            QuestionList.addView(viewHolder.getRoot());
            viewHolder.setOnAfterSecondsNotChangeSelection(1,
                    questionId ->
                    {
                        presenter.sendAnswer(currentattemptinfo, questionId);
                    }
            );
        }
    }

    @Override
    public void NavigateToLogin() {
        Intent i= new Intent(DoQuiz.this.getApplicationContext(), Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void ShowToast(String msg) {
        Toast.makeText(this,msg,LENGTH_SHORT).show();
    }

    @Override
    public void SaveAttemptInfo(AttemptQuizDTO info) {
        currentattemptinfo=info;
        presenter.ShowQuizInfo(info.getQuizId());
        presenter.showQuestion(info.getQuizId());
    }

    @Override
    public void ShowQuizInfo(QuizDTO info) {
        Title.setText(info.getName() != null ? info.getName() : "Chưa có tiêu đề");
        Description.setText(info.getDescription() != null ? info.getDescription() : "Không có mô tả");
        StartTime.setText("Bắt đầu: " + info.getStartTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        DueTime.setText("Kết thúc: " + info.getDueTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        CourseId.setText("Khóa học: " + info.getCourseName() + " (" + info.getCourseId() + ")");

    }

    @Override
    public void Finish() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void showAlertDialogToNotify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoQuiz.this);
        builder.setMessage("Bài làm của bạn đã tự động nộp do đã hết thời gian làm bài");

        builder.setPositiveButton("Quay lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Finish();
            }
        });

        builder.create();

        builder.show();
    }


    private void startCountdown(long millisUntilFinished) {
        new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                Timer.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                Timer.setText("00:00:00");
                showAlertDialogToNotify();
            }
        }.start();
    }
}
