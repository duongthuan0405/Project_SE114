package com.example.tqt_quiz.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public class CourseAdapter {
    private Context context;
    private List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
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
            holder.imgCourseAvatar = convertView.findViewById(R.id.img_CourseAvatar_ViewCourse);
            holder.tvCourseName = convertView.findViewById(R.id.tv_CourseName_ViewCourse);
            holder.tvHostName = convertView.findViewById(R.id.tv_HostName_ViewCourse);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Course course = courseList.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.tvHostName.setText(course.getHostName());
        holder.imgCourseAvatar.setImageResource(course.getAvatar());

        return convertView;
    }

}
