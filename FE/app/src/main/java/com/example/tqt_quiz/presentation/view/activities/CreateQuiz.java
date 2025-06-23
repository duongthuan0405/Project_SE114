package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
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
import androidx.fragment.app.FragmentActivity;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AnswerDTO;
import com.example.tqt_quiz.domain.dto.CreateAnswerRequest;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.Quiz;
import com.example.tqt_quiz.presentation.contract_vp.CreateQuizContract;
import com.example.tqt_quiz.presentation.database.DatabaseHelper;
import com.example.tqt_quiz.presentation.presenter.CreateQuizPresenter;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateQuiz extends AppCompatActivity implements CreateQuizContract.IView
{
    CreateQuizContract.IPresenter presenter;
    private LinearLayout questionListContainer;
    private Button Finish, Cancel, btnDelete;
    private EditText Title, Description, StartTime, DueTime, CourseId;
    private Switch isPublishSwitch;
    private String quiz_id = "";

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

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);
        presenter = new CreateQuizPresenter(this);

        try {
            quiz_id = getIntent().getExtras().getString("quizId", "");
        } catch (Exception e) {

        }



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

        DueTime.setText("2025-07-20 09:30");
        StartTime.setText("2025-07-20 09:15");
        Title.setText("TITLE");
        Description.setText("Description");

        if(!quiz_id.equals(""))
        {
            presenter.showOldQuestion(quiz_id);
        }

        addNewQuestion();
        Cancel.setOnClickListener(v -> finish());
        Finish.setOnClickListener(v -> handleSubmit());

    }

    private void addNewQuestion() {
        LayoutInflater inflater = LayoutInflater.from(getTheContext());
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

            radioButtons[0].setChecked(true);

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

    private void addOldQuestion(Question q)
    {
        View questionView = inflater.inflate(R.layout.item_question, questionListContainer, false);
        LinearLayout answerList = questionView.findViewById(R.id.rg_AnswerList_QuestionItem);
        EditText edTx_ContentQuestion = questionView.findViewById(R.id.edt_Content_QuestionItem);
        edTx_ContentQuestion.setText(q.getContent().toString());
        RadioButton[] radioButtons = new RadioButton[TOTAL_ANSWERS];
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            View answerView = inflater.inflate(R.layout.item_answer, answerList, false);

            EditText edtAnswer = answerView.findViewById(R.id.edt_Content_AnswerItem);
            RadioButton radioButton = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);

            edtAnswer.setText(q.getAnswers().get(i).getContent());
            ((android.widget.TextView) answerView.findViewById(R.id.tv_Label_AnswerItem)).setText(String.valueOf(labels[i]));

            radioButtons[i] = radioButton;
            radioButtons[i].setChecked(q.getAnswers().get(i).isCorrect());

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

    private void handleSubmit()
    {
        if(quiz_id.equals(""))
        {
            String title = "";
            String description = "";
            String startTime = "";
            String dueTime = "";
            boolean isPublished = false;
            String course_id;

            title = Title.getText().toString().trim();
            description = Description.getText().toString().trim();
            startTime = StartTime.getText().toString().trim();
            dueTime = DueTime.getText().toString().trim();
            isPublished = isPublishSwitch.isChecked();
            course_id = "836e796a4e";

            QuizCreateRequestDTO quizCreateRequestDTO;

            try {
                LocalDateTime lcDt_startTime = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime lcDt_dueTime = LocalDateTime.parse(dueTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                quizCreateRequestDTO = new QuizCreateRequestDTO(title, description, lcDt_startTime, lcDt_dueTime, course_id);
            }
            catch (Exception e)
            {
                Toast.makeText(CreateQuiz.this.getTheContext(), "Định dạng ngày sai", Toast.LENGTH_LONG).show();
                return;
            }


            if (title.isEmpty() || startTime.isEmpty() || dueTime.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }


            presenter.createQuiz(quizCreateRequestDTO);
        }
        else
        {

        }


    }

    @Override
    public void showOldQuestionOnUI(List<Question> oldQuestion) {
        for(Question q : oldQuestion)
        {
            addOldQuestion(q);
        }
        addNewQuestion();
    }

    @Override
    public Context getTheContext() {
        return CreateQuiz.this.getApplicationContext();
    }

    @Override
    public void CreateQuestionAnswer()
    {
        List<CreateQuestionRequest> questionList = new ArrayList<>();

        List<View> inValidQuestion = new ArrayList<>();

        for (int i = 0; i < questionListContainer.getChildCount(); i++) {
            View questionView = questionListContainer.getChildAt(i);
            EditText edtQuestionContent = questionView.findViewById(R.id.edt_Content_QuestionItem);
            LinearLayout answerListLayout = questionView.findViewById(R.id.rg_AnswerList_QuestionItem);

            String questionContent = edtQuestionContent.getText().toString().trim();
            if(questionContent.isEmpty())
            {
                inValidQuestion.add(questionView);
            }
            else {
                List<CreateAnswerRequest> answers = new ArrayList<>();

                for (int j = 0; j < answerListLayout.getChildCount(); j++) {
                    View answerView = answerListLayout.getChildAt(j);
                    EditText edtAnswerContent = answerView.findViewById(R.id.edt_Content_AnswerItem);
                    RadioButton rdbIsCorrect = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);

                    String answerContent = edtAnswerContent.getText().toString().trim();
                    if (answerContent.isEmpty()) {
                        inValidQuestion.add(questionView);
                    } else {
                        boolean isCorrect = rdbIsCorrect.isChecked();

                        if (!answerContent.isEmpty()) {
                            answers.add(new CreateAnswerRequest(answerContent, isCorrect));
                        }
                    }
                }

                questionList.add(new CreateQuestionRequest(questionContent, quiz_id, answers));
            }
        }

        if(inValidQuestion.isEmpty())
        {
            presenter.addQuestionAnswer(questionList);
        }
        else
        {
            Toast.makeText(CreateQuiz.this, "Vui lòng nhập đủ nội dung câu hỏi", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void navigateToLogin() {
        Intent i = new Intent(CreateQuiz.this, Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showMessage(String s) {
        Toast.makeText(CreateQuiz.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishAddQuiz() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        CreateQuiz.this.finish();
    }

    @Override
    public void initQuizIdInView(String id) {
        quiz_id = id;
    }
}
