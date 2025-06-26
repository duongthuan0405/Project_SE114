package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.presentation.contract_vp.ViewCourseStContract;

public class ViewCourseStPresenter implements ViewCourseStContract.IPresenter
{
    ViewCourseStContract.IView view;
    public ViewCourseStPresenter(ViewCourseStContract.IView v)
    {
        view = v;
    }

    @Override
    public void showCourseInfo(String courseId) {
        // Lấy thông tin course bằng id rồi kêu IView show nó lên ui (CourseRelatedInteractor)
    }

    @Override
    public void showListMemberOfCourse(String courseId) {
        // Lấy danh sách thành viên của khóa học rồi kêu IView hiện lên (CourseRelatedInteractor)
    }
}
