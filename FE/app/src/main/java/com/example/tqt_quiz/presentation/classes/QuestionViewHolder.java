package com.example.tqt_quiz.presentation.classes;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.tqt_quiz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
    private LinearLayout[] answerFrame = new LinearLayout[TOTAL_ANSWERS];
    private final Button btn_Add, btn_Delete;
    private final TextView tvCorrectAnswer;
    private final CardView layoutQuestionContainer;
    CountDownTimer timer;

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
            answerFrame[i] = answerView.findViewById(R.id.frameQuestion);
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

    public void setDataWithCorrectAnswerAndSelectedAnswer(Question dto)
    {
        data = dto;
        edtQuestion.setText(dto.getContent());
        for (int i = 0; i < TOTAL_ANSWERS; i++) {
            edtAnswers[i].setText(dto.getAnswers().get(i).getContent());
            rdbAnswers[i].setChecked(dto.getAnswers().get(i).isSelected());

            if(dto.getAnswers().get(i).isCorrect())
                answerFrame[i].setBackgroundColor(Color.parseColor("#93DC5C"));
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

    public void setOnAfterSecondsNotChangeSelection(int sec, IActionAfterNotChangeSelection action) {
        for(RadioButton r : rdbAnswers)
        {
            r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        if(timer != null) {
                            timer.cancel();
                        }
                        timer = getNewTimer(sec, action);
                        timer.start();
                    }
                }
            });
        }
    }

    public interface IActionAfterNotChangeSelection
    {
        public void onAction(String lastSelection);
    }

    private CountDownTimer getNewTimer(int sec, IActionAfterNotChangeSelection action)
    {
        return timer = new CountDownTimer(sec * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                action.onAction(QuestionViewHolder.this.getIdAnswerSelected());
            }
        };
    }

}
