package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.JoinCourseInteractIMP;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IJoinCourseInteract;
import com.example.tqt_quiz.presentation.contract_vp.JoinCourseSTContract;

public class JoinCourseStPresenter implements JoinCourseSTContract.IPresentor {
    JoinCourseSTContract.IView view;
    CourseRelatedInteractIMP courseRelatedInteractIMP;
    JoinCourseInteractIMP joinCourseInteractIMP;
    public JoinCourseStPresenter(JoinCourseSTContract.IView view)
    {
        this.view=view;
        courseRelatedInteractIMP=new CourseRelatedInteractIMP();
        joinCourseInteractIMP=new JoinCourseInteractIMP();
    }
    @Override
    public void SearchCourse(String Id) {
        courseRelatedInteractIMP.FindCourseByID(Id, view.GetContext(), new ICourseRelatedInteract.FindCourseByIDCallBack() {
            @Override
            public void onSuccess(CourseDTO response) {
                view.ShowSearchedCourse(response);
                view.EnableJoin();
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.ShowToast(msg);
                view.DisableJoin();
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.ShowToast("Bạn không thể truy cập tài nguyên này");
                view.DisableJoin();
            }

            @Override
            public void onFailureByNotExistCourse(String msg) {
                view.ShowToast(msg);
                view.DisableJoin();
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.ShowToast(msg);
                view.DisableJoin();
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Không thể kết nối đến máy chủ");
                view.DisableJoin();
            }
        });
    }

    @Override
    public void JoinCourse(String Id) {
        joinCourseInteractIMP.JoinCourse(Id, view.GetContext(), new IJoinCourseInteract.JoinCourseCallBack() {
            @Override
            public void onSuccess(JoinCourseResponseDTO response) {
                view.ShowToast(response.getState());
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.ShowToast(msg);
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole(String msg) {
                view.ShowToast("Bạn không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistCourse(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByServer(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Không thể kết nối đến máy chủ");
            }
        });
    }
}
