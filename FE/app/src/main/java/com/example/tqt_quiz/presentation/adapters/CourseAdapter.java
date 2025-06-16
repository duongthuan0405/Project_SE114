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
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    private Context context;
    private List<Course> courses;

    public CourseAdapter(@NonNull Context context, int resource, @NonNull List<Course> courses) {
        super(context, resource, courses);
        this.context = context;
        this.courses = courses;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView imgCourseAvatar;
        TextView tvCourseName, tvHostName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
            holder = new ViewHolder();
            holder.imgCourseAvatar = convertView.findViewById(R.id.img_Avatar_CourseItem);
            holder.tvCourseName = convertView.findViewById(R.id.tv_Name_CourseItem);
            holder.tvHostName = convertView.findViewById(R.id.tv_Teacher_CourseItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Course course = courses.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.tvHostName.setText(course.getHostName());
        holder.imgCourseAvatar.setImageResource(course.getAvatar());

        return convertView;
    }

}
