package com.example.tqt_quiz.presentation.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.ImageRelatedInteract;
import com.example.tqt_quiz.domain.dto.CourseCreateInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.UploadResponse;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IImageRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.CreateCourseContract;

public class CreateCoursePresenter implements CreateCourseContract.IPresenter
{
    CreateCourseContract.IView view;
    ICourseRelatedInteract courseRelatedInteract = null;
    IImageRelatedInteract imageRelatedInteract = null;
    public CreateCoursePresenter(CreateCourseContract.IView view)
    {
        this.view = view;
        courseRelatedInteract = new CourseRelatedInteractIMP();
        imageRelatedInteract = new ImageRelatedInteract();
    }

    @Override
    public void onCreateClick(CourseCreateInfo courseCreateInfo) {
        courseRelatedInteract.CreateNewCourse(courseCreateInfo, view.getTheContext(), new ICourseRelatedInteract.CreateNewCourseCallBack() {
            @Override
            public void onSuccess(CourseDTO response) {
                view.showSuccess();
                if(!response.getAvatar().equals("")) {
                    Uri uri = Uri.parse(response.getAvatar());
                    imageRelatedInteract.uploadLogo(uri, response.getId(), view.getTheContext(), new IImageRelatedInteract.UploadImageCallBack() {
                        @Override
                        public void onSuccess(UploadResponse response) {
                            view.finishAddCourse();
                        }

                        @Override
                        public void onFailureByExpiredToken() {
                            view.navigateToLogin();
                        }

                        @Override
                        public void onFailureByUnAcepptedRole() {
                            view.showMessage("Tài khoản không thể truy cập tài nguyên này");
                            view.finishAddCourse();
                        }

                        @Override
                        public void onFailureByOther(String msg) {
                            view.showMessage(msg);
                            view.finishAddCourse();
                        }

                        @Override
                        public void onFailureByCannotConnectToServer() {
                            view.showMessage("Không thể kết nối đến server");
                            view.finishAddCourse();
                        }
                    });
                }
                else
                {
                    view.finishAddCourse();
                }
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistAccount(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });
    }

}
