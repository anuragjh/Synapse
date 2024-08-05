package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class IntroWelcome extends AppCompatActivity {

    Button loginButton , signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_welcome);
        LottieAnimationView animationView = findViewById(R.id.cloudView);
        animationView.setVisibility(View.VISIBLE);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant));

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setOnClickListener(v->{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        signupButton.setOnClickListener(v->{
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

    }
}