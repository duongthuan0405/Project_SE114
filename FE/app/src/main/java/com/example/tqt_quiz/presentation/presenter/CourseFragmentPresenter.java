package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ICourseFragmentContract;

import java.util.List;

public class CourseFragmentPresenter implements ICourseFragmentContract.IPresenter
{
    public ICourseFragmentContract.IView view;
    public ICourseRelatedInteract courseRelatedInteract;
    public CourseFragmentPresenter(ICourseFragmentContract.IView view)
    {
        this.view = view;
        courseRelatedInteract = new CourseRelatedInteractIMP();
    }

    @Override
    public void showAllMyCourse() {
        courseRelatedInteract.FetchAllCourseHosted(view.getTheContext(), new ICourseRelatedInteract.FetchHostedCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                view.showAllList(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showError("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showError(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showError("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void onAddCourseClick() {

        view.navigateToAddCourse();
    }
}
