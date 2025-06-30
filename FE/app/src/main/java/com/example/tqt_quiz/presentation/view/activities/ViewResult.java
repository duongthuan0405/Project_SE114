package com.example.tqt_quiz.presentation.view.activities;

import static android.widget.Toast.LENGTH_LONG;

import android.annotation.SuppressLint;
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
import com.example.tqt_quiz.domain.dto.AttemptQuizDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.QuestionViewHolder;
import com.example.tqt_quiz.presentation.contract_vp.ViewResultContract;
import com.example.tqt_quiz.presentation.presenter.ViewResultPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewResult extends AppCompatActivity implements ViewResultContract.IView {

    private TextView Title, Description, StartTime, DueTime, CourseId, ResultSummary, TimeToDo;
    private LinearLayout QuestionList;
    private AttemptQuizDTO AttemptInfo;
    private ViewResultContract.IPresenter presenter;
    private String quizId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root_view_result), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);


        // Ánh xạ view
        Title = findViewById(R.id.tv_Title_ViewResult);
        Description = findViewById(R.id.tv_Description_ViewResult);
        StartTime = findViewById(R.id.tv_StartTime_ViewResult);
        DueTime = findViewById(R.id.tv_DueTime_ViewResult);
        CourseId = findViewById(R.id.tv_CourseId_ViewResult);
        ResultSummary = findViewById(R.id.tv_ResultSummary_ViewResult);
        QuestionList = findViewById(R.id.ll_QuestionList_ViewResult);
        TimeToDo = findViewById(R.id.tv_TimeToDo_ViewResult);



        presenter = new ViewResultPresenter(this);

        Intent intent = getIntent();
        quizId = intent.getStringExtra("quizId");

        presenter.GetAttempt(quizId);

/*
        // Nhận dữ liệu từ intent

        String title = intent.getStringExtra("quiz_name");
        String description = intent.getStringExtra("quiz_description");
        String start = intent.getStringExtra("quiz_start");
        String due = intent.getStringExtra("quiz_due");
        String courseId = intent.getStringExtra("course_id");

        // Hiển thị thông tin quiz
        Title.setText(title != null ? title : "Chưa có tiêu đề");
        Description.setText(description != null ? description : "Không có mô tả");
        StartTime.setText("Bắt đầu: " + start);
        DueTime.setText("Kết thúc: " + due);
        CourseId.setText("Course ID: " + (courseId != null ? courseId : "--"));

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


        // Tạo UI hiển thị từng câu hỏi
        LayoutInflater inflater = LayoutInflater.from(this);
        for (Question question : questionList) {
            View questionView = inflater.inflate(R.layout.item_question, QuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, false);

            // Set dữ liệu đã chọn
            viewHolder.setDataWithSelectedAnswer(question);
            viewHolder.setReadOnly();
            viewHolder.showResultFeedback();

            QuestionList.addView(viewHolder.getRoot());
        }

         */
    }

    @Override
    public Context GetTheContext() {
        return this;
    }

    @Override
    public void NavigateToLogin() {
        Intent i= new Intent(ViewResult.this.getApplicationContext(), Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void ShowQuizResult(List<QuestionDTO> questionlist) {
        int totalCorrect = getIntent().getIntExtra("totalCorrect", 0);
        ResultSummary.setText("Kết quả: " + totalCorrect + " / " + questionlist.size());

        if(AttemptInfo == null)
        {
            TimeToDo.setText("Thời gian: Chưa tham gia");
            TimeToDo.setTextColor(Color.parseColor("#F44336"));
        }
        else if(!AttemptInfo.isSubmitted())
        {
            TimeToDo.setText("Thời gian: Chưa nộp bài");
            TimeToDo.setTextColor(Color.parseColor("#F28705"));
        }
        else
        {
            Duration d = Duration.between(AttemptInfo.getAttemptTime(), AttemptInfo.getFinishTime());
            long totalSecs = d.getSeconds();
            long hours = totalSecs / 3600;
            long mins = (totalSecs - hours * 3600) / 60;
            long sec = totalSecs - hours * 3600 - mins * 60;
            TimeToDo.setText(String.format("Thời gian: %d giờ %d phút %d giây", hours, mins, sec ));
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        Question q = null;
        for (QuestionDTO question : questionlist) {
            q = new Question(question);
            View questionView = inflater.inflate(R.layout.item_question, QuestionList, false);
            QuestionViewHolder viewHolder = new QuestionViewHolder(questionView, false);
            viewHolder.disableForEditing();
            questionView.setTag(viewHolder);

            viewHolder.setDataWithCorrectAnswerAndSelectedAnswer(q);
            viewHolder.getRoot().findViewById(R.id.btn_Add_QuestionItem).setVisibility(View.GONE);
            viewHolder.getRoot().findViewById(R.id.btn_Delete_QuestionItem).setVisibility(View.GONE);
            QuestionList.addView(viewHolder.getRoot());
        }
    }

    @Override
    public void SaveAttemptInfo(AttemptQuizDTO info) {
        AttemptInfo=info;
        presenter.ShowQuizInfo(quizId);
        presenter.ShowQuizResult(quizId);
    }

    @Override
    public void ShowToast(String msg) {
        Toast.makeText(this,msg,LENGTH_LONG).show();
    }

    @Override
    public void ShowQuizInfo(QuizDTO info) {
        Title.setText(info.getName() != null ? info.getName() : "Chưa có tiêu đề");
        Description.setText(info.getDescription() != null ? info.getDescription() : "Không có mô tả");
        StartTime.setText("Bắt đầu: " + info.getStartTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        DueTime.setText("Kết thúc: " + info.getDueTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        CourseId.setText("Khóa học: " + (info.getCourseId() != null ? info.getCourseName() : "--"));
    }
}
