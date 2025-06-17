package com.example.tqt_quiz.staticclass;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.color.ColorRoles;

public class StaticClass
{
    public static void setImage(ImageView imageView, String imgURL, int ic_default)
    {
        Glide.with(imageView.getContext()).load(imgURL).error(ic_default).into(imageView);
    }

    public static class AccountTypeId
    {
        public static String admin      = "0000000000";
        public static String teacher    = "0000000001";
        public static String student    = "0000000002";
    }
}
