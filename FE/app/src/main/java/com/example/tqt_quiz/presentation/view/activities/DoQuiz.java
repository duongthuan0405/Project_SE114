package com.example.tqt_quiz.presentation.view.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.example.tqt_quiz.presentation.contract_vp.DoQuizContract;
import com.example.tqt_quiz.presentation.utils.DummyQuizGenerator;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.util.List;

public class DoQuiz extends AppCompatActivity implements DoQuizContract.IView {

    private TextView Title, Description, StartTime, DueTime, CourseId;
    private LinearLayout QuestionList;
    private Button Finish;
    private DoQuizContract.IPresenter presenter;
    private List<Question> questionList;
    private final QuestionViewHolder[] questionViewHolders = new QuestionViewHolder[20];
    private AttemptQuizDTO currentattemptinfo=null;

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

        // B1: Attempt Quiz

        // B2: Lay ds cau hoi dua vao Quiz (Quiz nam trong Quiz)

        // B3: Show info quiz

        // B4: Show questions (lấy dựa mã quiz)

        Intent intent = getIntent();
        String quizId=intent.getStringExtra("quizId");
        presenter.StartAttempt(quizId);
        presenter.ShowQuizInfo(quizId);
        presenter.ShowQuiz(quizId);


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
    }

    @Override
    public Context GetTheContext() {
        return this;
    }

    @Override
    public void ShowQuiz(List<QuestionDTO> questionlist) {
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
    }

    @Override
    public void ShowQuizInfo(QuizDTO info) {
        Title.setText(info.getName() != null ? info.getName() : "Chưa có tiêu đề");
        Description.setText(info.getDescription() != null ? info.getDescription() : "Không có mô tả");
        StartTime.setText("Bắt đầu: " + info.getStartTime());
        DueTime.setText("Kết thúc: " + info.getDueTime());
        CourseId.setText("Course ID: " + (info.getCourseId() != null ? info.getCourseId() : "--"));

    }
}
