package com.example.tqt_quiz.presentation.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.tqt_quiz.presentation.utils.DummyQuizGenerator;

import java.util.List;

public class DoQuiz extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvTime, tvCourseId;
    private LinearLayout llQuestionList;
    private Button btnFinish;

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

        // Ánh xạ view
        tvTitle = findViewById(R.id.tv_Title_DoQuiz);
        tvDescription = findViewById(R.id.tv_Description_DoQuiz);
        tvTime = findViewById(R.id.tv_Time_DoQuiz);
        tvCourseId = findViewById(R.id.tv_CourseId_DoQuiz);
        llQuestionList = findViewById(R.id.ll_QuestionList_DoQuiz);
        btnFinish = findViewById(R.id.btn_Finish_DoQuiz);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("quiz_name");
        String description = intent.getStringExtra("quiz_description");
        String start = intent.getStringExtra("quiz_start");
        String due = intent.getStringExtra("quiz_due");
        String courseId = intent.getStringExtra("quiz_courseId");

        // Hiển thị lên UI
        tvTitle.setText(title != null ? title : "Chưa có tiêu đề");
        tvDescription.setText(description != null ? description : "Không có mô tả");
        tvTime.setText("Thời gian: " + start + " - " + due);
        tvCourseId.setText("Course ID: " + (courseId != null ? courseId : "--"));

        // Load câu hỏi (Dùng tạm thời – sau này thay bằng API hoặc gì đó, đại loại vậy)
        questionList = DummyQuizGenerator.getSampleQuestions(); // bạn có thể thay bằng dữ liệu thực tế

        // Hiển thị danh sách câu hỏi
        LayoutInflater inflater = LayoutInflater.from(this);
        int index = 0;
        for (Question question : questionList) {
            View questionView = inflater.inflate(R.layout.item_question, llQuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, true);
            questionView.setTag(viewHolder);
            viewHolder.setDataWithSelectedAnswer(question);
            viewHolder.getRoot().findViewById(R.id.btn_Add_QuestionItem).setVisibility(View.GONE);
            viewHolder.getRoot().findViewById(R.id.btn_Delete_QuestionItem).setVisibility(View.GONE);
            llQuestionList.addView(viewHolder.getRoot());
            questionViewHolders[index++] = viewHolder;
        }

        btnFinish.setOnClickListener(v -> {
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
    }
}
