package com.example.tqt_quiz.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.classes.Course;

import java.util.List;

public class CourseAdapterForSpinner extends ArrayAdapter<Course>
{
    public CourseAdapterForSpinner(@NonNull Context context, @NonNull List<Course> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        Course c = getItem(position);
            if(c != null)
            {
                TextView tv_NameCourse = v.findViewById(android.R.id.text1);
            tv_NameCourse.setText(c.getName());
        }
        return v;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null)
        {
            LayoutInflater li = LayoutInflater.from(getContext());
            v = li.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        Course c = getItem(position);
        if(c != null)
        {
            TextView tv_NameCourse = v.findViewById(android.R.id.text1);
            tv_NameCourse.setText(c.getName());
        }
        return v;

    }
}
