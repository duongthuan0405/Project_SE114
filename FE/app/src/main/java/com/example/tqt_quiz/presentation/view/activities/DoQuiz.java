package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.QuestionViewHolder;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoQuiz extends AppCompatActivity {

    private TextView Title, Description, StartTime, DueTime, CourseId, Timer;
    private LinearLayout QuestionList;
    private Button Finish;

    private List<Question> questionList;
    private final QuestionViewHolder[] questionViewHolders = new QuestionViewHolder[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_do_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root_do_quiz), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        // Ánh xạ view
        Title = findViewById(R.id.tv_Title_DoQuiz);
        Description = findViewById(R.id.tv_Description_DoQuiz);
        StartTime = findViewById(R.id.tv_StartTime_DoQuiz);
        DueTime = findViewById(R.id.tv_DueTime_DoQuiz);
        CourseId = findViewById(R.id.tv_CourseId_DoQuiz);
        QuestionList = findViewById(R.id.ll_QuestionList_DoQuiz);
        Finish = findViewById(R.id.btn_Finish_DoQuiz);
        Timer = findViewById(R.id.tv_Timer_DoQuiz);

        // B1: Attempt Quiz

        // B2: Lay ds cau hoi dua vao Quiz (Quiz nam trong Quiz)

        // B3: Show info quiz

        // B4: Show questions (lấy dựa mã quiz)








        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("quiz_name");
        String description = intent.getStringExtra("quiz_description");
        String start = intent.getStringExtra("quiz_start");
        String due = intent.getStringExtra("quiz_due");
        String courseId = intent.getStringExtra("quiz_courseId");

        // Hiển thị lên UI
        Title.setText(title != null ? title : "Chưa có tiêu đề");
        Description.setText(description != null ? description : "Không có mô tả");
        StartTime.setText("Bắt đầu: " + start);
        DueTime.setText("Kết thúc: " + due);
        CourseId.setText("Course ID: " + (courseId != null ? courseId : "--"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat);
        LocalDateTime dueTime = LocalDateTime.parse(due, formatter);
        LocalDateTime now = LocalDateTime.now();

        long millisUntilDue = Duration.between(now, dueTime).toMillis();
        if (millisUntilDue > 0) {
            startCountdown(millisUntilDue);
        } else {
            Timer.setText("00:00");
            Finish.setEnabled(false);
        }

        /*
        // Load câu hỏi (Dùng tạm thời – sau này thay bằng API hoặc gì đó, đại loại vậy)
        questionList = DummyQuizGenerator.getSampleQuestions(); // bạn có thể thay bằng dữ liệu thực tế

        // Hiển thị danh sách câu hỏi
        LayoutInflater inflater = LayoutInflater.from(this);
        int index = 0;
        for (Question question : questionList) {

            View questionView = inflater.inflate(R.layout.item_question, QuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, false);
            questionView.setTag(viewHolder);

            viewHolder.setDataWithSelectedAnswer(question);

            viewHolder.getRoot().findViewById(R.id.btn_Add_QuestionItem).setVisibility(View.GONE);
            viewHolder.getRoot().findViewById(R.id.btn_Delete_QuestionItem).setVisibility(View.GONE);
            QuestionList.addView(viewHolder.getRoot());
            questionViewHolders[index++] = viewHolder;
        }

        Finish.setOnClickListener(v -> {
            int score = 0;
            for (int i = 0; i < questionList.size(); i++) {
                QuestionViewHolder holder = questionViewHolders[i];
                if (holder != null) {
                    String selectedId = holder.getIdAnswerSelected();
                    String correctId = questionList.get(i).getCorrectAnswerId();
                    if (selectedId != null && selectedId.equals(correctId)) {
                        score++;
                    }
                }
            }

            double finalScore = (double) score / questionList.size() * 10;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("quiz_score", String.format("%.1f", finalScore));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

         */
    }

    private void startCountdown(long millisUntilFinished) {
        new CountDownTimer(millisUntilFinished, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long remainingSeconds = seconds % 60;

                Timer.setText(String.format("%02d:%02d", minutes, remainingSeconds));
            }

            @Override
            public void onFinish() {
                Timer.setText("00:00");
                Finish.performClick();
            }
        }.start();
    }
}
