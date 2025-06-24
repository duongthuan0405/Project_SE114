package com.example.tqt_quiz.presentation.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.CreateAnswerRequest;
import com.example.tqt_quiz.domain.dto.CreateQuestionRequest;
import com.example.tqt_quiz.domain.dto.QuestionDTO;
import com.example.tqt_quiz.domain.dto.QuizCreateRequestDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.presentation.adapters.CourseAdapterForSpinner;
import com.example.tqt_quiz.presentation.classes.Answer;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.classes.Question;
import com.example.tqt_quiz.presentation.classes.QuestionViewHolder;
import com.example.tqt_quiz.presentation.contract_vp.CreateQuizContract;
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
    private EditText Title, Description, StartTime, DueTime;
    private Spinner CourseId;
    private Switch isPublishSwitch;
    private String quiz_id = "";

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

        Title = findViewById(R.id.edt_QuizTitle_CreateQuiz);
        Description = findViewById(R.id.edt_QuizDescription_CreateQuiz);
        questionListContainer = findViewById(R.id.ll_QuestionList_CreateQuiz);
        StartTime = findViewById(R.id.edt_StartTime_CreateQuiz);
        DueTime = findViewById(R.id.edt_DueTime_CreateQuiz);
        CourseId = findViewById(R.id.spn_Course);
        isPublishSwitch = findViewById(R.id.switch_IsPublish_CreateQuiz);
        Finish = findViewById(R.id.btn_Finish_CreateQuiz);
        btnDelete = findViewById(R.id.btn_Delete_CreateQuiz);
        Cancel = findViewById(R.id.btn_Cancel_CreateQuiz);

        presenter.loadCourseList();

        try {
            quiz_id = getIntent().getExtras().getString("quizId", "");
        } catch (Exception e)
        {

        }


        if(!quiz_id.equals(""))
        {
            presenter.onDetailOldQuiz(quiz_id);
        }

        else {
            addNewQuestion();
            Cancel.setOnClickListener(v -> finish());
            Finish.setOnClickListener(v -> handleSubmit());
        }

    }

    private void addNewQuestion() {
        LayoutInflater inflater = LayoutInflater.from(getTheContext());
        View questionView = inflater.inflate(R.layout.item_question, questionListContainer, false);
        QuestionViewHolder questionViewHolder = new QuestionViewHolder(questionView, true);
        questionView.setTag(questionViewHolder);

        questionListContainer.addView(questionViewHolder.getRoot());

        questionViewHolder.setOnClickListenerForBtnAdd(v -> addNewQuestion());
        questionViewHolder.setOnClickListenerForBtnDelete(v ->
            {
                if(questionListContainer.getChildCount() > 1) questionListContainer.removeView(questionView);
            });
    }

    private void handleSubmit()
    {
        String title = "";
        String description = "";
        String startTime = "";
        String dueTime = "";
        boolean isPublished = false;

        String course_id = ((Course)CourseId.getSelectedItem()).getId();
        title = Title.getText().toString().trim();
        description = Description.getText().toString().trim();
        startTime = StartTime.getText().toString().trim();
        dueTime = DueTime.getText().toString().trim();
        isPublished = isPublishSwitch.isChecked();

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

        if(quiz_id.equals(""))
        {
            presenter.createQuiz(quizCreateRequestDTO);
        }
        else
        {
            presenter.updateQuiz(quiz_id, quizCreateRequestDTO);
        }


    }


    @Override
    public Context getTheContext() {
        return CreateQuiz.this.getApplicationContext();
    }

    @Override
    public void CreateQuestionAnswer()
    {
        List<CreateQuestionRequest> questionList = new ArrayList<>();

        List<QuestionViewHolder> inValidQuestion = new ArrayList<>();

        for (int i = 0; i < questionListContainer.getChildCount(); i++) {
            QuestionViewHolder questionViewHolder = (QuestionViewHolder) questionListContainer.getChildAt(i).getTag();

            Question q = questionViewHolder.collectData();

            String questionContent = q.getContent().toString().trim();
            if(questionContent.isEmpty())
            {
                inValidQuestion.add(questionViewHolder);
            }
            else {
                List<CreateAnswerRequest> answers = new ArrayList<>();

                for(Answer a : q.getAnswers())
                {
                    a.setCorrect(a.isSelected());
                    answers.add(new CreateAnswerRequest(a.getContent(), a.isCorrect()));
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

    @Override
    public void showQuizInfo(QuizDTO response)
    {
        DueTime.setText(response.getDueTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        StartTime.setText(response.getStartTime().format(DateTimeFormatter.ofPattern(StaticClass.DateTimeFormat)));
        Title.setText(response.getName());
        Description.setText(response.getDescription());
        presenter.onGetOldQuestion(response.getId());
    }

    @Override
    public void showQuestion(List<QuestionDTO> response)
    {
        for (QuestionDTO q : response)
        {
            LayoutInflater inflater = LayoutInflater.from(getTheContext());
            View questionView = inflater.inflate(R.layout.item_question, questionListContainer, false);
            QuestionViewHolder questionViewHolder = new QuestionViewHolder(questionView, true);
            questionView.setTag(questionViewHolder);


            Question question = new Question(q);

            questionViewHolder.setDataWithCorrectAnswer(question);

            questionListContainer.addView(questionView);
            questionViewHolder.setOnClickListenerForBtnAdd(v -> addNewQuestion());
            questionViewHolder.setOnClickListenerForBtnDelete(v ->
                {
                    if(questionListContainer.getChildCount() > 1)
                        questionListContainer.removeView(questionView);
                }
            );
        }

        Cancel.setOnClickListener(v -> finish());
        Finish.setOnClickListener(v -> handleSubmit());
    }

    @Override
    public void loadOnSpinner(List<CourseDTO> response) {
        List<Course> list = new ArrayList<>();
        for(CourseDTO c : response)
        {
            list.add(new Course(c));
        }

        CourseAdapterForSpinner adt = new CourseAdapterForSpinner(getTheContext(), list);
        CourseId.setAdapter(adt);
        CourseId.setSelection(0);
    }
}
