package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText emailEditText;
    private ImageView emailCheckIcon;
    private ImageView emailIcon;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant));

        emailEditText = findViewById(R.id.emailEditText);
        emailCheckIcon = findViewById(R.id.emailCheckIcon);
        emailIcon = findViewById(R.id.emailIcon);
        nextButton = findViewById(R.id.nextButton);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());


        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidEmail(s.toString())) {
                    emailCheckIcon.setVisibility(View.VISIBLE);
                    emailIcon.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.colorPrimaryVariant));
                } else {
                    emailCheckIcon.setVisibility(View.GONE);
                    emailIcon.setColorFilter(ContextCompat.getColor(SignupActivity.this, R.color.colorSecondaryText));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (isValidEmail(email)) {
                    Intent intent = new Intent(SignupActivity.this, DetailsSignUp.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
