package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.CreateCourseContract;
import com.example.tqt_quiz.presentation.view.activities.CreateCourse;

public class CreateCoursePresenter implements CreateCourseContract.IPresenter
{
    CreateCourseContract.IView view;
    ICourseRelatedInteract courseRelatedInteract = null;
    public CreateCoursePresenter(CreateCourseContract.IView view)
    {
        this.view = view;
        courseRelatedInteract = new CourseRelatedInteractIMP();
    }


    @Override
    public void onCreateClick(CourseCreateInfo courseCreateInfo) {
        courseRelatedInteract.CreateNewCourse(courseCreateInfo, view.getTheContext(), new ICourseRelatedInteract.CreateNewCourseCallBack() {
            @Override
            public void onSuccess(CourseDTO response) {
                view.showSuccess();
                view.finishAddCourse();
            }

            @Override
            public void onFailureByExpiredToken(String msg) {

            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {

            }

            @Override
            public void onFailureByNotExistAccount(String msg) {

            }

            @Override
            public void onFailureByServerError(String msg) {

            }

            @Override
            public void onFailureByCannotSendToServer() {

            }
        });
    }
}
