package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class AccountCreated extends AppCompatActivity {

    private static final int DISPLAY_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_created);


        new Handler().postDelayed(() -> {
            startActivity(new Intent(AccountCreated.this, MainActivity.class));
            finish();
        }, DISPLAY_DURATION);
    }
}
