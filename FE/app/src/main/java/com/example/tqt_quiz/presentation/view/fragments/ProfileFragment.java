package com.example.tqt_quiz.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private AccountInfo accountInfo;

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
        ChangePassword = v.findViewById(R.id.btn_ChangePassword_Profile);

        presenter = new ProfileFragmentPresenter(this);

        //Khởi tạo Launcher
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        onTabReload();
                        if(o.getResultCode() == getActivity().RESULT_OK)
                        {
                            Toast.makeText(getTheContext(), "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        changePasswordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == getActivity().RESULT_OK)
                    {
                        Toast.makeText(getTheContext(), "Sửa mật khẩu thành công", Toast.LENGTH_LONG).show();
                        onTabReload();
                    }
                }
        );

        EditInfo.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ChangeProfile.class);
            intent.putExtra("oldProfile", accountInfo);
            editProfileLauncher.launch(intent);
        });

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
        accountInfo = response;
        FullName.setText(response.getFullName());
        AccountType.setText(response.getAccountType());
        Email.setText(response.getEmail());
        StaticClass.setImage(Avatar, response.getAvatar(), R.drawable.resource_default);
    }

    @Override
    public void showError(String msg) {
        Toast.makeText(getTheContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetCurrentInfoSuccess(AccountInfo response) {
    }

    @Override
    public void onTabReload() {
        presenter.getProfile();
    }
}