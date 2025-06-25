package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ICourseFragmentStContract;

import java.util.List;

public class CourseFragmentStPresenter implements ICourseFragmentStContract.IPresenter {

    private final ICourseFragmentStContract.IView view;
    private final ICourseRelatedInteract courseRelatedInteract;

    public CourseFragmentStPresenter(ICourseFragmentStContract.IView view) {
        this.view = view;
        this.courseRelatedInteract = new CourseRelatedInteractIMP();
    }

    @Override
    public void loadJoinedCourses() {
        courseRelatedInteract.getCourses_Joined(view.getTheContext(), new ICourseRelatedInteract.GetCourseListCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.showCourseList(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.showError("Bạn không có quyền truy cập.");
            }

            @Override
            public void onFailureByOther(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer() {
                view.showError("Không thể kết nối đến server.");
            }
        });
    }

    @Override
    public void loadPendingCourses() {
        courseRelatedInteract.getCourses_Pending(view.getTheContext(), new ICourseRelatedInteract.GetCourseListCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.showCourseList(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.showError("Bạn không có quyền truy cập.");
            }

            @Override
            public void onFailureByOther(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer() {
                view.showError("Không thể kết nối đến server.");
            }
        });
    }
}
