package com.example.tqt_quiz.presentation.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.domain.dto.AccountInfo;
import com.example.tqt_quiz.presentation.classes.IReloadableTab;
import com.example.tqt_quiz.presentation.contract_vp.ProfileFragmentContract;
import com.example.tqt_quiz.presentation.presenter.ProfileFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.ChangePassword;
import com.example.tqt_quiz.presentation.view.activities.ChangeProfile;
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment implements ProfileFragmentContract.IView, IReloadableTab
{
    ProfileFragmentContract.IPresenter presenter;
    Button Logout;
    TextView FullName;
    TextView AccountType;
    TextView Email;
    ShapeableImageView Avatar;
    Button EditInfo, ChangePassword;
    ActivityResultLauncher<Intent> editProfileLauncher, changePasswordLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        Logout = v.findViewById(R.id.btn_Logout);
        FullName = v.findViewById(R.id.tv_Name_Profile);
        AccountType = v.findViewById(R.id.tv_AccountType_Profile);
        Email = v.findViewById(R.id.tv_Email_Profile);
        Avatar = v.findViewById(R.id.img_Avatar_Profile);
        EditInfo = v.findViewById(R.id.btn_EditInfo_Profile);

        presenter = new ProfileFragmentPresenter(this);

        //Khởi tạo Launcher
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        presenter.getMySelfAccountInfo();
                    }
                }
        );

        changePasswordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );

        EditInfo.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), ChangeProfile.class);
            editProfileLauncher.launch(intent);
        });

        ChangePassword = v.findViewById(R.id.btn_ChangePassword_Profile);
        ChangePassword.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), ChangePassword.class);
            changePasswordLauncher.launch(intent);
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogoutClick();
            }
        });

        presenter.getMySelfAccountInfo();

        return v;
    }

    private final ActivityResultLauncher<Intent> changeProfileLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String firstName = data.getStringExtra("first_name");
                    String middleName = data.getStringExtra("middle_name");
                    String email = data.getStringExtra("email");
                    Uri avatarUri = data.getData(); // getData dùng cho URI từ ảnh

                    // Cập nhật giao diện
                    FullName.setText(middleName + " " + firstName);
                    Email.setText(email);
                    if (avatarUri != null) {
                        Avatar.setImageURI(avatarUri);
                    }
                }
            });

    @Override
    public Context getTheContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void navigationToLogin() {
        FragmentActivity parent = getActivity();
        Intent i = new Intent(parent.getApplicationContext(), Login.class);
        i.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
        );
        startActivity(i);
    }

    @Override
    public void showInfo(AccountInfo response) {
        FullName.setText(response.getFullName());
        AccountType.setText(response.getAccountType());
        Email.setText(response.getEmail());
        StaticClass.setImage(Avatar, response.getAvatar(), R.drawable.resource_default);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onTabReload() {

    }
}