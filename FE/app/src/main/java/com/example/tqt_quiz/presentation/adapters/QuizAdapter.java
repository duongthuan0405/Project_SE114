package com.example.tqt_quiz.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Quiz;

import java.util.List;

public class QuizAdapter extends ArrayAdapter<Quiz> {
    private Context context;
    private List<Quiz> quizzes;

    public QuizAdapter(@NonNull Context context, int resource, @NonNull List<Quiz> quizzes) {
        super(context, resource, quizzes);
        this.context = context;
        this.quizzes = quizzes;
    }

    @Override
    public int getCount() {
        return quizzes.size();
    }

    @Override
    public Quiz getItem(int position) {
        return quizzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imgQuizIcon;
        TextView tvQuizName, tvStartTime, tvEndTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
            holder = new ViewHolder();
            holder.imgQuizIcon = convertView.findViewById(R.id.img_Icon_QuizItem);
            holder.tvQuizName = convertView.findViewById(R.id.tv_Name_QuizItem);
            holder.tvStartTime = convertView.findViewById(R.id.tv_StartTime_QuizItem);
            holder.tvEndTime = convertView.findViewById(R.id.tv_EndTime_QuizItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Quiz quiz = getItem(position);
        holder.tvQuizName.setText(quiz.getName());
        holder.tvStartTime.setText("Bắt đầu: " + quiz.getStartTime());
        holder.tvEndTime.setText("Kết thúc: " + quiz.getDueTime());

        // Icon mặc định đã gán sẵn trong item_quiz.xml → không cần set lại ở đây
        // Nếu bạn muốn đảm bảo: holder.imgQuizIcon.setImageResource(R.drawable.ic_test);

        return convertView;
    }
}