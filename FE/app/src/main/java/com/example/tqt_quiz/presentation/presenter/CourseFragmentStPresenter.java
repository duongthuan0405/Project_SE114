package com.example.tqt_quiz.presentation.presenter;

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
        courseRelatedInteract.FetchAllCourseJoined(view.getTheContext(), new ICourseRelatedInteract.FetchJoinedCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.showCourseList(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Bạn không có quyền truy cập chức năng này.");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError("Lỗi máy chủ: " + msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showError("Không thể kết nối tới server.");
            }
        });
    }

    @Override
    public void loadPendingCourses() {
        courseRelatedInteract.FetchAllCoursePending(view.getTheContext(), new ICourseRelatedInteract.FetchAllPendingCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.showCourseList(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Bạn không có quyền truy cập chức năng này.");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError("Lỗi máy chủ: " + msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showError("Không thể kết nối tới server.");
            }
        });
    }
}
