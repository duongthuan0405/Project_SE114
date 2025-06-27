package com.example.tqt_quiz.presentation.contract_vp;

import android.content.Context;
import android.net.Uri;

import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;

import java.util.List;

public interface ViewCourseContract
{
    public interface IView
    {

        Context getTheContext();

        void showCourseInfo(CourseDTO response);

        void navigateToLogin();

        void showToast(String s);

        void showListForAllMember(List<AccountInfo> response);

        void showListMemberPending(List<AccountInfo> response);

        void reloadList();

        void Finish();
    }

    public interface IPresenter
    {

        void showCourseInfo(String courseId);

        void showListMemberOfCourse(String courseId);

        void showListMemberPending(String courseId);

        void onAcceptAccount(String accountId, String courseId);

        void onDenyAccount(String accountId, String courseId);

        void saveLogo(Uri selectedImageUri, String courseId);
    }
}
