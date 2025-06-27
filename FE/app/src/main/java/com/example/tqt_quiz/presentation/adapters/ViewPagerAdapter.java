package com.example.tqt_quiz.presentation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tqt_quiz.presentation.view.fragments.CourseFragment;
import com.example.tqt_quiz.presentation.view.fragments.NotificationFragment;
import com.example.tqt_quiz.presentation.view.fragments.ProfileFragment;
import com.example.tqt_quiz.presentation.view.fragments.QuizFragment;
import com.example.tqt_quiz.presentation.view.fragments.CourseFragmentSt;
import com.example.tqt_quiz.staticclass.StaticClass;


import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> list;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        list = new ArrayList<>();

        //Phân quyền người dùng
        String roleId = StaticClass.accountInfo.getAccountTypeId();
        if (roleId.equals(StaticClass.AccountTypeId.teacher)) {
            list.add(new CourseFragment());  // Giáo viên
        } else {
            list.add(new CourseFragmentSt()); // Học sinh
        }

        list.add(new QuizFragment());
        list.add(new NotificationFragment());
        list.add(new ProfileFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public Fragment getFragmentAt(int i)
    {
        return list.get(i);
    }

}
