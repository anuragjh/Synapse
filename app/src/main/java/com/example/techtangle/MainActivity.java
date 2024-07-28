package com.example.techtangle;

import android.animation.Animator;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lottieAnimationView = findViewById(R.id.lottie_animation_view);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorOnPrimary));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            // Show animation based on selected item
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.playAnimation();
            return true;
        });
    }
}
