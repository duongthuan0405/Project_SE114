package com.example.tqt_quiz.presentation.view.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;


import com.example.tqt_quiz.R;
import com.example.tqt_quiz.presentation.adapters.ViewPagerAdapter;
import com.example.tqt_quiz.presentation.classes.IReloadableTab;
import com.example.tqt_quiz.staticclass.StaticClass;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainHome extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_home);

        StaticClass.customActionBar(getSupportActionBar(), R.layout.custom_action_bar_2);

        tabLayout = findViewById(R.id.tb_Tabbar_MainHome);
        viewPager2 = findViewById(R.id.viewpager_MainHome);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Khóa học");
                    tab.setIcon(R.drawable.tab_course_selector);
                    break;
                case 1:
                    tab.setText("Bài kiểm tra");
                    tab.setIcon(R.drawable.tab_quiz_selector);
                    break;
                case 2:
                    tab.setText("Tài khoản");
                    tab.setIcon(R.drawable.tab_profile_selector);
                    break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                reload(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                reload(tab.getPosition(), false);
            }
        });
    }

    private void reload(int position, boolean b)
    {
        Fragment f = viewPagerAdapter.getFragmentAt(position);
        if (!(f instanceof IReloadableTab)) return;

        // Đã STARTED hoặc RESUMED ⇒ view chắc chắn đã inflate xong
        if (f.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((IReloadableTab) f).onTabReload();
            return;
        }

        // Chưa tới STARTED ⇒ đợi
        f.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_START) {
                    ((IReloadableTab) f).onTabReload();
                    f.getLifecycle().removeObserver(this);   // chỉ chạy 1 lần
                }
            }
        });
    }
}