package com.example.techtangle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtangle.utils.AndroidUtil;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000;
    private static final String SHARED_PREFS = "my_shared_prefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (isLoggedIn()) {
                String username = AndroidUtil.getUsername(this);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                startActivity(new Intent(SplashActivity.this, IntroWelcome.class));
            }
            finish();
        }, SPLASH_DURATION);
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }
}
