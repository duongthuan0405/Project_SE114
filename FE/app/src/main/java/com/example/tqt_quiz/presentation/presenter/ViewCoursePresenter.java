package com.example.tqt_quiz.presentation.presenter;

import android.net.Uri;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.ImageRelatedInteract;
import com.example.tqt_quiz.data.interactor.JoinCourseInteractIMP;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.UploadResponse;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IImageRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IJoinCourseInteract;
import com.example.tqt_quiz.presentation.contract_vp.ViewCourseContract;

import java.util.List;

public class ViewCoursePresenter implements ViewCourseContract.IPresenter
{
    ViewCourseContract.IView view;
    ICourseRelatedInteract courseRelatedInteract;
    IJoinCourseInteract joinCourseInteract;
    IImageRelatedInteract imageRelatedInteract;
    public ViewCoursePresenter(ViewCourseContract.IView view)
    {
        this.view = view;
        courseRelatedInteract = new CourseRelatedInteractIMP();
        joinCourseInteract = new JoinCourseInteractIMP();
        imageRelatedInteract = new ImageRelatedInteract();
    }

    @Override
    public void showCourseInfo(String courseId) {
        courseRelatedInteract.FindCourseByID(courseId, view.getTheContext(), new ICourseRelatedInteract.FindCourseByIDCallBack() {
            @Override
            public void onSuccess(CourseDTO response) {
                response.setDescription(response.getDescription());
                view.showCourseInfo(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showToast("Tài khoản không có quyền truy cập tài nguyên này");
                view.Finish();
            }

            @Override
            public void onFailureByNotExistCourse(String msg) {
                view.showToast(msg);
                view.Finish();
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showToast(msg);
                view.Finish();
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showToast("Không thể kết nối đến server");
                view.Finish();
            }
        });
    }

    @Override
    public void showListMemberOfCourse(String courseId) {

        courseRelatedInteract.GetAllMemberInCourse(courseId, view.getTheContext(), new ICourseRelatedInteract.GetAllMemberInCourseCallBack() {
            @Override
            public void onSuccess(List<AccountInfo> response) {
                view.showListForAllMember(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.showToast("Tài khoản không tể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByOtherError(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showToast("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void showListMemberPending(String courseId) {
        joinCourseInteract.ViewAllAccountPendingThisCourse(courseId, view.getTheContext(), new IJoinCourseInteract.ViewAllAccountPendingThisCourse() {
            @Override
            public void onSuccess(List<AccountInfo> response) {
                view.showListMemberPending(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnacceptedRole(String msg) {
                view.showToast("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByNotExistCourse(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onCannotContactWithServer() {
                view.showToast("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void onAcceptAccount(String accountId, String courseId) {
        joinCourseInteract.AcceptCourseJoinRequest(accountId, courseId, view.getTheContext(), new IJoinCourseInteract.AcceptCourseCallBack() {
            @Override
            public void onSuccess(String msg) {
                view.showToast("Đã chấp nhận tài khoản tham gia khóa học");
                view.reloadList();
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onCannotConnectToServer(String msg) {
                view.showToast("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void onDenyAccount(String accountId, String courseId) {
        joinCourseInteract.DenyCourseJoinRequest(accountId, courseId, view.getTheContext(), new IJoinCourseInteract.DenyCourseCallBack() {
            @Override
            public void onSuccess(String msg) {
                view.showToast("Đã từ chối tài khoản tham gia khóa học");
                view.reloadList();
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onCannotConnectToServer(String msg) {
                view.showToast("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void saveLogo(Uri selectedImageUri, String courseId) {
        imageRelatedInteract.uploadLogo(selectedImageUri, courseId, view.getTheContext(), new IImageRelatedInteract.UploadImageCallBack() {
            @Override
            public void onSuccess(UploadResponse response) {
                view.showToast("Lưu ảnh thành công");
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.showToast("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByOther(String msg) {
                view.showToast(msg);
            }

            @Override
            public void onFailureByCannotConnectToServer() {
                view.showToast("Không thể kết nối đến server");
            }
        });
    }
}
