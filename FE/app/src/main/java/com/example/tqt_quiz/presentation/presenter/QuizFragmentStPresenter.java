package com.example.tqt_quiz.presentation.presenter;

import android.util.Log;

import com.example.tqt_quiz.data.interactor.CourseRelatedInteractIMP;
import com.example.tqt_quiz.data.interactor.QuizRelatedIMP;
import com.example.tqt_quiz.domain.dto.CourseDTO;
import com.example.tqt_quiz.domain.dto.QuizDTO;
import com.example.tqt_quiz.domain.interactor.ICourseRelatedInteract;
import com.example.tqt_quiz.domain.interactor.IQuizRelatedInteract;
import com.example.tqt_quiz.presentation.classes.Course;
import com.example.tqt_quiz.presentation.contract_vp.QuizFragmentStContract;
import com.example.tqt_quiz.staticclass.StaticClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuizFragmentStPresenter implements QuizFragmentStContract.IPresenter
{
    QuizFragmentStContract.IView view;
    ICourseRelatedInteract courseRelatedInteract;
    IQuizRelatedInteract quizRelatedInteract;
    public QuizFragmentStPresenter(QuizFragmentStContract.IView view)
    {
        this.view = view;
        courseRelatedInteract = new CourseRelatedInteractIMP();
        quizRelatedInteract = new QuizRelatedIMP();
    }

    @Override
    public void loadCourseToSpinner() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("", "Tất cả", "", false, "", ""));
        courseRelatedInteract.FetchAllCourseJoined(view.getTheContext(), new ICourseRelatedInteract.FetchJoinedCallBack() {
            @Override
            public void onSuccess(List<CourseDTO> response) {
                for(CourseDTO c : response)
                {
                    courses.add(new Course(c));
                }
                view.showOnCourseSpinner(courses);
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

    @Override
    public void getAllQuizByFilter(String status) {
        quizRelatedInteract.GetAllQuiz(view.getTheContext(), new IQuizRelatedInteract.GetAllQuizCallBack() {
            @Override
            public void onSuccess(List<QuizDTO> response) {
                List<QuizDTO> quizzes;
                if(status.equals(StaticClass.StateOfQuiz.SOON))
                {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isBefore(q.getStartTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }
                else if (status.equals(StaticClass.StateOfQuiz.NOW)) {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isAfter(q.getStartTime()) && LocalDateTime.now().isBefore(q.getDueTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }
                else
                {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isAfter(q.getDueTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }

                view.showQuiz(quizzes);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });

    }

    @Override
    public void getQuizByFilter(String selectedCourse, String selectedStatus) {
        quizRelatedInteract.FetchALlQuizOfACourse(selectedCourse, view.getTheContext(), new IQuizRelatedInteract.FetchALlQuizOfACourseCallBack() {
            @Override
            public void onSuccess(List<QuizDTO> response) {
                List<QuizDTO> quizzes;
                if(selectedStatus.equals(StaticClass.StateOfQuiz.SOON))
                {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isBefore(q.getStartTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }
                else if (selectedStatus.equals(StaticClass.StateOfQuiz.NOW)) {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isAfter(q.getStartTime()) && LocalDateTime.now().isBefore(q.getDueTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }
                else
                {
                    quizzes = new ArrayList<>();
                    for(QuizDTO q : response)
                    {
                        if(LocalDateTime.now().isAfter(q.getDueTime()))
                        {
                            quizzes.add(q);
                        }
                    }
                }

                view.showQuiz(quizzes);
            }

            @Override
            public void onFailureByExpiredToken() {
                view.navigateToLogin();
            }

            @Override
            public void onFailureByUnAcceptedRole() {
                view.showMessage("Tài khoản không thể truy cập tài nguyên này");
            }

            @Override
            public void onOtherFailure(String msg) {
                view.showMessage(msg);
            }

            @Override
            public void onFailureByCannotSendToServer() {
                view.showMessage("Không thể kết nối đến server");
            }
        });

    }


}
