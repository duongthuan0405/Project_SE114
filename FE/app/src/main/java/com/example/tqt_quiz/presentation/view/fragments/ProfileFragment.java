package com.example.tqt_quiz.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
import com.example.tqt_quiz.presentation.view.activities.Login;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment implements ProfileFragmentContract.IView, IReloadableTab
{
    ProfileFragmentContract.IPresenter presenter;
    Button btn_Logout;
    TextView txVw_FullName;
    TextView txVw_AccountType;
    TextView txVw_Email;
    ShapeableImageView imVw_Avatar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        btn_Logout = v.findViewById(R.id.btn_Logout);
        txVw_FullName = v.findViewById(R.id.tv_Name_Profile);
        txVw_AccountType = v.findViewById(R.id.tv_AccountType_Profile);
        txVw_Email = v.findViewById(R.id.tv_Email_Profile);
        imVw_Avatar = v.findViewById(R.id.img_Avatar_Profile);


        presenter = new ProfileFragmentPresenter(this);
        btn_Logout.setOnClickListener(new View.OnClickListener() {
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
        txVw_FullName.setText(response.getFullName());
        txVw_AccountType.setText(response.getAccountType());
        txVw_Email.setText(response.getEmail());
        StaticClass.setImage(imVw_Avatar, response.getAvatar(), R.drawable.resource_default);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onTabReload() {

    }
}