package com.example.tqt_quiz.presentation.view.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;

public class CreateQuiz extends AppCompatActivity {

    private LinearLayout questionListContainer;
    private Button Finish, Cancel;
    private EditText Title, Description;
    private LayoutInflater inflater;

    private static final int TOTAL_ANSWERS = 4;
    private char[] labels = {'A', 'B', 'C', 'D'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        Title = findViewById(R.id.edt_QuizTitle_CreateQuiz);
        Description = findViewById(R.id.edt_QuizDescription_CreateQuiz);
        questionListContainer = findViewById(R.id.ll_QuestionList_CreateQuiz);
        Finish = findViewById(R.id.btn_Finish_CreateQuiz);
        Cancel = findViewById(R.id.btn_Cancel_CreateQuiz);

        inflater = LayoutInflater.from(this);

        // Thêm 1 câu hỏi mặc định ban đầu
        addNewQuestion();

        // Xử lý nút hoàn thành hoặc hủy
        Cancel.setOnClickListener(v -> finish());
        Finish.setOnClickListener(v -> handleSubmit());
    }

    private void addNewQuestion() {
        View questionView = inflater.inflate(R.layout.item_question, questionListContainer, false);

        LinearLayout answerList = questionView.findViewById(R.id.ll_AnswerList_QuestionItem);
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            View answerView = inflater.inflate(R.layout.item_answer, answerList, false);

            EditText edtAnswer = answerView.findViewById(R.id.edt_Content_AnswerItem);
            RadioButton radioButton = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);
            ((EditText) answerView.findViewById(R.id.edt_Content_AnswerItem)).setHint("Đáp án " + labels[i]);
            ((android.widget.TextView) answerView.findViewById(R.id.tv_Label_AnswerItem)).setText(String.valueOf(labels[i]));

            // Đảm bảo chỉ chọn được 1 radio button đúng trong mỗi câu hỏi
            radioGroup.addView(radioButton);
            answerList.addView(answerView);
        }

        // Nút xóa câu hỏi
        Button btnDelete = questionView.findViewById(R.id.btn_Delete_QuestionItem);
        btnDelete.setOnClickListener(v -> questionListContainer.removeView(questionView));

        questionListContainer.addView(questionView);
    }

    private void handleSubmit() {
        // TODO: Thu thập dữ liệu từ các EditText/RadioButton và xử lý tạo Quiz
    }
}
