package com.example.tqt_quiz.presentation.presenter;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.presentation.contract_vp.ViewCourseStContract;

import java.util.List;

public class ViewCourseStPresenter implements ViewCourseStContract.IPresenter
{
    ViewCourseStContract.IView view;
    CourseRelatedInteractIMP courseRelatedInteractIMP;
    public ViewCourseStPresenter(ViewCourseStContract.IView v)
    {
        view = v;
        courseRelatedInteractIMP=new CourseRelatedInteractIMP();
    }

    @Override
    public void showCourseInfo(String courseId) {
        // Lấy thông tin course bằng id rồi kêu IView show nó lên ui (CourseRelatedInteractor)
        courseRelatedInteractIMP.FindCourseByID(courseId, view.GetTheContext(), new ICourseRelatedInteract.FindCourseByIDCallBack() {
            @Override
            public void onSuccess(CourseDTO response) {
                view.ShowCourse(response);
            }

            @Override
            public void onFailureByExpiredToken(String msg) {
                view.ShowToast(msg);
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByNotExistCourse(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByServerError(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Không thể kết nối đến máy chủ");
            }
        });
    }

    @Override
    public void showListMemberOfCourse(String courseId) {
        // Lấy danh sách thành viên của khóa học rồi kêu IView hiện lên (CourseRelatedInteractor)
        courseRelatedInteractIMP.GetAllMemberInCourse(courseId, view.GetTheContext(), new ICourseRelatedInteract.GetAllMemberInCourseCallBack() {
            @Override
            public void onSuccess(List<AccountInfo> response) {
                view.ShowAllMemBerInCourse(response);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.ShowToast("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByOtherError(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Không thể kết nối đến server");
            }
        });
    }

    @Override
    public void LeaveCourse(String courseId) {
        courseRelatedInteractIMP.LeaveCourse(courseId, view.GetTheContext(), new ICourseRelatedInteract.LeaveCourseCallBack() {
            @Override
            public void onSuccess() {
                view.ShowToast("Đã rời khóa học thành công");
                view.Finish();
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcepptedRole() {
                view.ShowToast("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onFailureByOtherError(String msg) {
                view.ShowToast(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.ShowToast("Không thể kết nối đến server");
            }
        });
    }
}



