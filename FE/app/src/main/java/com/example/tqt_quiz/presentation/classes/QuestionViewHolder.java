package com.example.tqt_quiz.presentation.classes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tqt_quiz.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionViewHolder
{
    private static final int TOTAL_ANSWERS = 4;
    private char[] labels = {'A', 'B', 'C', 'D'};
    private boolean isSetChooseDefault = false;
    private String[] idAnswer = new String[TOTAL_ANSWERS];
    Question data;
    View root;
    private final EditText edtQuestion;
    private final RadioGroup answerList;
    private final EditText[] edtAnswers = new EditText[TOTAL_ANSWERS];
    private RadioButton[] rdbAnswers = new RadioButton[TOTAL_ANSWERS];
    private final Button btn_Add, btn_Delete;
    private final TextView tvCorrectAnswer;
    private final LinearLayout layoutQuestionContainer;

    public QuestionViewHolder(View root, boolean isSetChooseDefault)
    {
        this.root = root;
        this.isSetChooseDefault = isSetChooseDefault;

        edtQuestion = root.findViewById(R.id.edt_Content_QuestionItem);
        answerList = root.findViewById(R.id.rg_AnswerList_QuestionItem);
        btn_Add = root.findViewById(R.id.btn_Add_QuestionItem);
        btn_Delete = root.findViewById(R.id.btn_Delete_QuestionItem);
        tvCorrectAnswer = root.findViewById(R.id.tv_CorrectAnswer_QuestionItem);
        layoutQuestionContainer = root.findViewById(R.id.layout_question_item);

        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            View answerView = LayoutInflater.from(root.getContext()).inflate(R.layout.item_answer, answerList, false);

            edtAnswers[i] = answerView.findViewById(R.id.edt_Content_AnswerItem);
            rdbAnswers[i] = answerView.findViewById(R.id.rdb_IsCorrect_AnswerItem);
            TextView tv_AnswerItem = answerView.findViewById(R.id.tv_Label_AnswerItem);
            tv_AnswerItem.setText(labels[i] + "");

            int finalI = i;
            rdbAnswers[i].setOnClickListener(v -> {
                // bảo đảm chỉ 1 đáp án đúng
                for (int j = 0; j < TOTAL_ANSWERS; j++) {
                    rdbAnswers[j].setChecked(j == finalI);
                }
            });
            answerList.addView(answerView);
        }
        if(isSetChooseDefault)
        {
            rdbAnswers[0].setChecked(true);
        }
    }

    public View getRoot()
    {
        return  this.root;
    }

    public void setDataWithCorrectAnswer(Question dto)
    {
        data = dto;
        edtQuestion.setText(dto.getContent());
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            edtAnswers[i].setText(dto.getAnswers().get(i).getContent());
            rdbAnswers[i].setChecked(dto.getAnswers().get(i).isCorrect());
            idAnswer[i] = dto.getAnswers().get(i).getId();
        }
    }

    public void setReadOnly() {
        edtQuestion.setEnabled(false);
        btn_Add.setVisibility(View.GONE);
        btn_Delete.setVisibility(View.GONE);
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            edtAnswers[i].setEnabled(false);
            rdbAnswers[i].setEnabled(false);
        }
    }


    public void setDataWithSelectedAnswer(Question dto) {
        data = dto;
        edtQuestion.setText(dto.getContent());
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            edtAnswers[i].setText(dto.getAnswers().get(i).getContent());
            rdbAnswers[i].setChecked(dto.getAnswers().get(i).isSelected());
            idAnswer[i] = dto.getAnswers().get(i).getId();
        }
    }

    public Question collectData() {

        Question dto = new Question("", null);

        dto.setId(data != null ? data.getId() : null); // giữ lại ID nếu có
        dto.setContent(edtQuestion.getText().toString());

        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            Answer ans = new Answer();
            ans.setContent(edtAnswers[i].getText().toString());
            ans.setSelected(rdbAnswers[i].isChecked());
            ans.setQuestionId(dto.getId());
            ans.setId(idAnswer[i] == null ? "" : idAnswer[i]);
            answers.add(ans);
        }
        dto.setAnswers(answers);
        return dto;
    }

    public void showResultFeedback() {
        String correctAnswer = "";
        String selectedAnswer = getIdAnswerSelected();

        boolean isCorrect = false;

        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            Answer ans = data.getAnswers().get(i);
            if (ans.isCorrect()) {
                correctAnswer = labels[i] + ". " + ans.getContent();
            }
            if (ans.isCorrect() && ans.getId().equals(selectedAnswer)) {
                isCorrect = true;
            }
        }

        if (!isCorrect) {
            // Câu sai → viền đỏ + hiện đáp án đúng
            layoutQuestionContainer.setBackgroundResource(R.drawable.rounded_border_red);
            tvCorrectAnswer.setText("Đáp án đúng: " + correctAnswer);
            tvCorrectAnswer.setVisibility(View.VISIBLE);
        } else {
            // Câu đúng → viền bình thường
            layoutQuestionContainer.setBackgroundResource(R.drawable.rounded_border);
            tvCorrectAnswer.setVisibility(View.GONE);
        }
    }

    public String getIdAnswerSelected()
    {
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            if(rdbAnswers[i].isChecked())
                return idAnswer[i];
        }
        return "";
    }

    public void setOnClickListenerForBtnAdd(View.OnClickListener lsn)
    {
        btn_Add.setOnClickListener(lsn);
    }

    public void setOnClickListenerForBtnDelete(View.OnClickListener lsn)
    {
        btn_Delete.setOnClickListener(lsn);
    }

    public void disableForEditing() {
        disableForAllChildren(root);
        btn_Delete.setVisibility(View.GONE);
        btn_Add.setVisibility(View.GONE);
    }

    private void disableForAllChildren(View view)
    {
        view.setEnabled(false);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                disableForAllChildren(group.getChildAt(i));
            }
        }
    }
}
