package com.example.tqt_quiz.presentation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tqt_quiz.presentation.view.fragments.CourseFragment;
import com.example.tqt_quiz.presentation.view.fragments.NotificationFragment;
import com.example.tqt_quiz.presentation.view.fragments.ProfileFragment;
import com.example.tqt_quiz.presentation.view.fragments.QuizFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new CourseFragment();
            case 1: return new QuizFragment();
            case 2: return new NotificationFragment();
            case 3: return new ProfileFragment();
            default: return new CourseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
