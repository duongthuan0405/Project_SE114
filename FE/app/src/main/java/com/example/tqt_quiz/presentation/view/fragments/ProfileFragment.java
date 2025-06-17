package com.example.tqt_quiz.presentation.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.contract_vp.ProfileFragmentContract;
import com.example.tqt_quiz.presentation.presenter.ProfileFragmentPresenter;
import com.example.tqt_quiz.presentation.view.activities.Login;

public class ProfileFragment extends Fragment implements ProfileFragmentContract.IView
{
    ProfileFragmentContract.IPresenter presenter;
    Button btn_Logout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        btn_Logout = v.findViewById(R.id.btn_Logout);

        presenter = new ProfileFragmentPresenter(this);
        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogoutClick();
            }
        });

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
}