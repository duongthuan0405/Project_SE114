package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.JoinCourseInteractIMP;
import com.example.tqt_quiz.domain.APIService.JoinCourseRelatedService;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.JoinCourseResponseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IJoinCourseInteract;
import com.example.tqt_quiz.presentation.contract_vp.JoinCourseSTContract;

public class JoinCourseSTPresenter implements JoinCourseSTContract.IPresentor {
    JoinCourseSTContract.IView view;
    CourseRelatedInteractIMP courseRelatedInteractIMP;
    JoinCourseInteractIMP joinCourseInteractIMP;
    public JoinCourseSTPresenter(JoinCourseSTContract.IView view)
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
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.ShowToast(msg);
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
                view.ShowToast("Khong The Ket Noi Voi May Chu");
                view.DisableJoin();
            }
        });
    }

    @Override
    public void JoinCourse(String Id) {
        joinCourseInteractIMP.JoinCourse(Id, view.GetContext(), new IJoinCourseInteract.JoinCourseCallBack() {
            @Override
            public void onSuccess(JoinCourseResponseDTO response) {
                view.ShowToast("Gui yeu cau -tham gia thanh cong khoa hoc voi Id: "+response.getCourseID());
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByUnAcceptedRole(String msg) {
                view.ShowToast(msg);
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
                view.ShowToast("Khong The Ket Noi Voi May Chu");
            }
        });
    }
}
