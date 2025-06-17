package com.example.tqt_quiz.staticclass;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class StaticClass
{
    public static void setImage(ImageView imageView, String imgURL, int ic_default)
    {
        Glide.with(imageView.getContext()).load(imgURL).error(ic_default).into(imageView);
    }
}
