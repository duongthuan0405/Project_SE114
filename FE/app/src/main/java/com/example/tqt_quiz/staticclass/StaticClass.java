package com.example.tqt_quiz.staticclass;


import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;

import com.bumptech.glide.Glide;
import com.example.tqt_quiz.domain.dto.AccountInfo;


public class StaticClass
{
    public static void setImage(ImageView imageView, String imgURL, int ic_default)
    {
        Glide.with(imageView.getContext()).load(BareUrl + imgURL).error(ic_default).into(imageView);
        Log.d("THUAN", BareUrl + imgURL);
    }

    public static class AccountTypeId
    {
        public static String admin      = "0000000000";
        public static String teacher    = "0000000001";
        public static String student    = "0000000002";
    }
    public static void customActionBar(ActionBar ab, int layout_custom)
    {
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setCustomView(layout_custom);
    }

    public static class StateOfQuiz
    {
        public static String SOON = "Chưa diễn ra";
        public static String NOW = "Đang diễn ra";
        public static String END = "Đã kết thúc";
    }

    public static AccountInfo accountInfo;

    public static String DateTimeFormat = "yyyy-MM-dd HH:mm";

    public static String BareUrl = "http://10.0.2.2:5027";

}
