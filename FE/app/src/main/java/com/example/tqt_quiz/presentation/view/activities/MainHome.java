package com.example.tqt_quiz.presentation.view.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tqt_quiz.presentation.fragments.CourseFragment;
import com.example.tqt_quiz.presentation.fragments.QuizFragment;
import com.example.tqt_quiz.presentation.fragments.NotificationFragment;
import com.example.tqt_quiz.presentation.fragments.ProfileFragment;

import com.example.tqt_quiz.R;
import com.example.tqt_quiz.databinding.ActivityMainHomeBinding;

public class MainHome extends AppCompatActivity {

    ActivityMainHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new CourseFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.Course){
                replaceFragment(new CourseFragment());
            } else if (item.getItemId() == R.id.Quiz) {
                replaceFragment(new QuizFragment());
            } else if (item.getItemId() == R.id.Notification){
                replaceFragment(new NotificationFragment());
            } else if (item.getItemId() == R.id.Profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}