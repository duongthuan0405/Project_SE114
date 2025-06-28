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
import com.example.tqt_quiz.staticclass.StaticClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        TextView tvQuizName, tvStartTime, tvEndTime, tvStatus, tvCourseId;
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
            holder.tvStatus = convertView.findViewById(R.id.tv_Status_QuizItem);
            holder.tvCourseId = convertView.findViewById(R.id.tv_Course_QuizItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Quiz quiz = getItem(position);
        if (quiz != null) {
            holder.tvQuizName.setText(quiz.getName());
            holder.tvStartTime.setText("Bắt đầu: " + quiz.getStartTime());
            holder.tvEndTime.setText("Kết thúc: " + quiz.getDueTime());
            holder.tvCourseId.setText("Khóa học: " + quiz.getCourseName());

            String startTimeStr = quiz.getStartTime();  // VD: "2025-06-19 14:00"
            String endTimeStr = quiz.getDueTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date now = new Date();
                Date start = sdf.parse(startTimeStr);
                Date end = sdf.parse(endTimeStr);

                String statusText;
                int bgResId;

                if(!quiz.isPublished())
                {
                    statusText = StaticClass.StateOfQuiz.BENOTPUBLISHED;
                    bgResId = R.drawable.bg_status_benotpublished;
                }
                else if (now.before(start)) {
                    statusText = StaticClass.StateOfQuiz.SOON;
                    bgResId = R.drawable.bg_status_upcoming;
                } else if (now.after(end)) {
                    statusText = StaticClass.StateOfQuiz.END;
                    bgResId = R.drawable.bg_status_ended;
                } else {
                    statusText = StaticClass.StateOfQuiz.NOW;
                    bgResId = R.drawable.bg_status_ongoing;
                }

                holder.tvStatus.setText(statusText);
                holder.tvStatus.setBackgroundResource(bgResId);

            } catch (ParseException e) {
                holder.tvStatus.setText("Không rõ");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_upcoming);
            }
        }

        // Icon mặc định đã gán sẵn trong item_quiz.xml → không cần set lại ở đây
        // Nếu bạn muốn đảm bảo: holder.imgQuizIcon.setImageResource(R.drawable.ic_test);

        return convertView;
    }
}