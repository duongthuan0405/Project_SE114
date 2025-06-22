package com.example.tqt_quiz.presentation.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.database.DatabaseHelper;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.util.ArrayList;
import java.util.List;

public class CreateQuiz extends AppCompatActivity {

    private LinearLayout questionListContainer;
    private Button Finish, Cancel, btnDelete;
    private EditText Title, Description, StartTime, DueTime, CourseId;
    private Switch isPublishSwitch;

    private LayoutInflater inflater;
    private DatabaseHelper dbHelper;
    private String editingQuizId = null;

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
        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        // Ánh xạ
        Title = findViewById(R.id.edt_QuizTitle_CreateQuiz);
        Description = findViewById(R.id.edt_QuizDescription_CreateQuiz);
        questionListContainer = findViewById(R.id.ll_QuestionList_CreateQuiz);
        StartTime = findViewById(R.id.edt_StartTime_CreateQuiz);
        DueTime = findViewById(R.id.edt_DueTime_CreateQuiz);
        CourseId = findViewById(R.id.edt_CourseID_CreateQuiz);
        isPublishSwitch = findViewById(R.id.switch_IsPublish_CreateQuiz);
        Finish = findViewById(R.id.btn_Finish_CreateQuiz);
        btnDelete = findViewById(R.id.btn_Delete_CreateQuiz);
        Cancel = findViewById(R.id.btn_Cancel_CreateQuiz);

        inflater = LayoutInflater.from(this);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null) {
            editingQuizId = intent.getStringExtra("quiz_id");
            String name = intent.getStringExtra("quiz_name");
            String description = intent.getStringExtra("quiz_description");
            String start = intent.getStringExtra("quiz_start");
            String due = intent.getStringExtra("quiz_due");
            String courseID = intent.getStringExtra("quiz_course_id");
            boolean isPublic = intent.getBooleanExtra("quiz_is_public", false);

            if (name != null) Title.setText(name);
            if (description != null) Description.setText(description);
            if (start != null) StartTime.setText(start);
            if (due != null) DueTime.setText(due);
            isPublishSwitch.setChecked(isPublic);
            if (courseID != null) CourseId.setText(courseID);
        }

        if (intent != null && intent.hasExtra("quiz_id")) {
            // Được mở từ QuizFragment bằng cách chọn item
            btnDelete.setEnabled(true);
            btnDelete.setBackgroundResource(R.drawable.btn_rectagle_red); // hoặc custom màu đỏ

            btnDelete.setOnClickListener(v -> {
                long quizId = Long.parseLong(intent.getStringExtra("quiz_id"));
                DatabaseHelper db = new DatabaseHelper(this);
                db.deleteQuestionsAndAnswers(quizId);
                db.getWritableDatabase().delete(DatabaseHelper.TABLE_QUIZ, DatabaseHelper.COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});
                Toast.makeText(this, "Đã xóa bài kiểm tra", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("quiz_id", quizId);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                finish();
            });
        } else {
            // Mở từ nút "Thêm"
            btnDelete.setEnabled(false);
            btnDelete.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray));
        }

        // Thêm 1 câu hỏi mặc định ban đầu
        addNewQuestion();

        // Xử lý nút hoàn thành hoặc hủy
        Cancel.setOnClickListener(v -> finish());
        Finish.setOnClickListener(v -> handleSubmit());
    }

    private void addNewQuestion() {
        View questionView = inflater.inflate(R.layout.item_question, questionListContainer, false);

        LinearLayout answerList = questionView.findViewById(R.id.rg_AnswerList_QuestionItem);
        RadioButton[] radioButtons = new RadioButton[TOTAL_ANSWERS];

        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            View answerView = inflater.inflate(R.layout.item_answer, answerList, false);

            EditText edtAnswer = answerView.findViewById(R.id.edt_Content_AnswerItem);
            RadioButton radioButton = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);

            edtAnswer.setHint("Đáp án " + labels[i]);
            ((android.widget.TextView) answerView.findViewById(R.id.tv_Label_AnswerItem)).setText(String.valueOf(labels[i]));

            radioButtons[i] = radioButton;
            int finalI = i;
            radioButton.setOnClickListener(v -> {
                for (int j = 0; j < TOTAL_ANSWERS; j++) {
                    if (j != finalI) {
                        radioButtons[j].setChecked(false);
                    }
                }
            });

            answerList.addView(answerView);
        }

        // Nút xóa câu hỏi
        Button btnDelete = questionView.findViewById(R.id.btn_Delete_QuestionItem);
        btnDelete.setOnClickListener(v -> questionListContainer.removeView(questionView));

        // Nút thêm câu hỏi (nằm trong mỗi câu hỏi)
        Button btnAdd = questionView.findViewById(R.id.btn_Add_QuestionItem);
        btnAdd.setOnClickListener(v -> addNewQuestion());

        questionListContainer.addView(questionView);
    }

    private void handleSubmit() {
        String title = Title.getText().toString().trim();
        String description = Description.getText().toString().trim();
        String startTime = StartTime.getText().toString().trim();
        String dueTime = DueTime.getText().toString().trim();
        String courseId = CourseId.getText().toString().trim();
        boolean isPublished = isPublishSwitch.isChecked();

        if (title.isEmpty() || startTime.isEmpty() || dueTime.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < questionListContainer.getChildCount(); i++) {
            View questionView = questionListContainer.getChildAt(i);
            EditText edtQuestionContent = questionView.findViewById(R.id.edt_Content_QuestionItem);
            LinearLayout answerListLayout = questionView.findViewById(R.id.rg_AnswerList_QuestionItem);

            String questionContent = edtQuestionContent.getText().toString().trim();
            List<Answer> answers = new ArrayList<>();

            for (int j = 0; j < answerListLayout.getChildCount(); j++) {
                View answerView = answerListLayout.getChildAt(j);
                EditText edtAnswerContent = answerView.findViewById(R.id.edt_Content_AnswerItem);
                RadioButton rdbIsCorrect = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);

                String answerContent = edtAnswerContent.getText().toString().trim();
                boolean isCorrect = rdbIsCorrect.isChecked();

                if (!answerContent.isEmpty()) {
                    answers.add(new Answer(answerContent, isCorrect));
                }
            }

            if (!questionContent.isEmpty() && answers.size() == 4) {
                questionList.add(new Question(questionContent, answers));
            }
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất 1 câu hỏi hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Quiz quiz = new Quiz(null, title, description, startTime, dueTime, isPublished, courseId);

        // Lưu xuống database
        dbHelper = new DatabaseHelper(this);
        long quizId;
        if (editingQuizId != null && !editingQuizId.equals("-1")) {
            quiz.setId(editingQuizId);
            dbHelper.updateQuiz(quiz);
            dbHelper.deleteQuestionsAndAnswers(Long.parseLong(editingQuizId)); // cần ép về long
            quizId = Long.parseLong(editingQuizId);
        } else {
            quizId = dbHelper.insertQuiz(quiz);
        }

        if (quizId == -1) {
            Toast.makeText(this, "Lỗi khi lưu bài kiểm tra", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Question q : questionList) {
            long questionId = dbHelper.insertQuestion(quizId, q);
            for (Answer a : q.getAnswers()) {
                dbHelper.insertAnswer(questionId, a);
            }
        }

        Toast.makeText(this, "Lưu bài kiểm tra thành công", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("quiz_id", quizId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
