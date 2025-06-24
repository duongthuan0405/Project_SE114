package com.example.tqt_quiz.presentation.classes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AnswerDTO;
import com.example.tqt_quiz.domain.dto.QuestionDTO;

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

    public QuestionViewHolder(View root, boolean isSetChooseDefault)
    {
        this.root = root;
        this.isSetChooseDefault = isSetChooseDefault;

        edtQuestion = root.findViewById(R.id.edt_Content_QuestionItem);
        answerList = root.findViewById(R.id.rg_AnswerList_QuestionItem);
        btn_Add = root.findViewById(R.id.btn_Add_QuestionItem);
        btn_Delete = root.findViewById(R.id.btn_Delete_QuestionItem);

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

}
