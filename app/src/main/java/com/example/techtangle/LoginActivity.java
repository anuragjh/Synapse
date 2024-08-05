package com.example.techtangle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.techtangle.utils.AndroidUtil;
import com.example.techtangle.utils.NetworkUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, signupButton;
    private ImageView backButton, passwordVisibilityIcon ,usernameIcon ,passwordIcon;
    private EditText usernameEditText, passwordEditText;
    private TextView dividerLine, dividerText,forgotPasswordTextView;
    private LottieAnimationView progressBar;
    private boolean isPasswordVisible = false;

    private FirebaseFirestore db;
    private static final String SHARED_PREFS = "my_shared_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorSecondaryVariant));

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        //icon color
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameIcon = findViewById(R.id.usernameIcon);
        passwordIcon = findViewById(R.id.passwordIcon);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    usernameIcon.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorSecondaryText));
                } else {
                    usernameIcon.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorSecondaryVariant));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    passwordIcon.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorSecondaryText));
                } else {
                    passwordIcon.setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.colorSecondary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        dividerText = findViewById(R.id.dividerText);
        dividerLine = findViewById(R.id.dividerLine);
        forgotPasswordTextView= findViewById(R.id.forgotPasswordTextView);
        passwordVisibilityIcon = findViewById(R.id.passwordVisibilityIcon);
        passwordVisibilityIcon.setOnClickListener(view -> togglePasswordVisibility());

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> loginUser());

        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        forgotPasswordTextView.setOnClickListener(v->{
            Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
            startActivity(intent);
        });

        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.visibility_visible);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordVisibilityIcon.setImageResource(R.drawable.visibility_gone);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.length());
    }

    private void loginUser() {
        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        setInProgress(true);

        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    setInProgress(false);

                    if (task.isSuccessful()) {
                        Log.d("Firestore", "Query successful, documents found: " + task.getResult().size());
                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String storedPassword = document.getString("password");

                            if (password.equals(storedPassword)) {
                                // Store username in static variable and SharedPreferences
                                AndroidUtil.setUsername(this, username);

                                saveLoginDetails(username, password);

                                // Navigate to main activity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("Firestore", "Query failed: " + task.getException().getMessage());
                        Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);
            signupButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
            signupButton.setEnabled(true);
        }
    }

    private void saveLoginDetails(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
}
