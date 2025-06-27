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
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.QuestionViewHolder;
import com.example.tqt_quiz.presentation.utils.DummyQuizGenerator;

import java.util.List;

public class ViewResult extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvTime, tvCourseId, tvResultSummary;
    private LinearLayout llQuestionList;

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
        tvResultSummary = findViewById(R.id.tv_ResultSummary_DoQuiz);
        llQuestionList = findViewById(R.id.ll_QuestionList_DoQuiz);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("quiz_name");
        String description = intent.getStringExtra("quiz_description");
        String start = intent.getStringExtra("quiz_start");
        String due = intent.getStringExtra("quiz_due");
        String courseId = intent.getStringExtra("course_id");

        // Hiển thị thông tin quiz
        tvTitle.setText(title != null ? title : "Chưa có tiêu đề");
        tvDescription.setText(description != null ? description : "Không có mô tả");
        tvTime.setText("Thời gian: " + start + " - " + due);
        tvCourseId.setText("Course ID: " + (courseId != null ? courseId : "--"));

        // Lấy danh sách câu hỏi (demo)
        List<Question> questionList = DummyQuizGenerator.getSampleQuestions();

        // Đếm số câu đúng
        int correct = 0;
        for (Question q : questionList) {
            for (Answer a : q.getAnswers()) {
                if (a.isCorrect() && a.isSelected()) {
                    correct++;
                    break;
                }
            }
        }

        // Hiển thị kết quả
        tvResultSummary.setText("Kết quả: " + correct + " / " + questionList.size());

        // Tạo UI hiển thị từng câu hỏi
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Question question : questionList) {
            View questionView = inflater.inflate(R.layout.item_question, llQuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, false);

            // Set dữ liệu đã chọn
            viewHolder.setDataWithSelectedAnswer(question);
            viewHolder.setReadOnly();
            viewHolder.showResultFeedback();

            llQuestionList.addView(viewHolder.getRoot());
        }
    }
}
