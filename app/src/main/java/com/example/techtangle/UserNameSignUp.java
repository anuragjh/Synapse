package com.example.techtangle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.techtangle.model.UserModel;
import com.example.techtangle.utils.AndroidUtil;
import com.example.techtangle.utils.NetworkUtil;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserNameSignUp extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_PASSWORD = "password";

    private EditText usernameEditText;
    private TextView usernameAvailabilityTextView;
    private Button finishButton;
    private ImageView backButton;
    private LottieAnimationView progressBar;
    private FirebaseFirestore db;

    private String email, name, dob, profession, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            // Redirect to MainActivity if already logged in
            startActivity(new Intent(UserNameSignUp.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_user_name_sign_up);

        // Initialize views
        finishButton = findViewById(R.id.finishButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        usernameAvailabilityTextView = findViewById(R.id.usernameAvailabilityTextView);
        progressBar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();

        // Retrieve previous extras
        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            email = incomingIntent.getStringExtra("email");
            name = incomingIntent.getStringExtra("name");
            dob = incomingIntent.getStringExtra("dob");
            profession = incomingIntent.getStringExtra("profession");
            password = incomingIntent.getStringExtra("password");
        }

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        // Set up text watcher to check username availability
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 6) {
                    checkUsernameAvailability(s.toString().trim());
                } else {
                    usernameAvailabilityTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle finish button click
        finishButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            if (username.isEmpty()) {
                usernameEditText.setError("Enter Username");
                return;
            }
            checkUsernameAvailability(username, true);
        });
    }

    private void checkUsernameAvailability(String username) {
        checkUsernameAvailability(username, false);
    }

    private void checkUsernameAvailability(String username, boolean proceedAfterCheck) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(UserNameSignUp.this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            usernameAvailabilityTextView.setText("Username is available");
                            usernameAvailabilityTextView.setTextColor(getResources().getColor(R.color.colorAccent1));
                            usernameAvailabilityTextView.setVisibility(View.VISIBLE);
                            if (proceedAfterCheck) {
                                saveUserDetails(username);
                            }
                        } else {
                            usernameAvailabilityTextView.setText("Username is not available");
                            usernameAvailabilityTextView.setTextColor(getResources().getColor(R.color.colorError));
                            usernameAvailabilityTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(UserNameSignUp.this, "Error checking username", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserDetails(String username) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(UserNameSignUp.this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        setInProgress(true);

        UserModel user = new UserModel(name, username, email, dob, profession, password);

        db.collection("users")
                .document(username)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    AndroidUtil.setUsername(this, username);
                    setInProgress(false);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(KEY_IS_LOGGED_IN, true);
                    editor.putString(KEY_PASSWORD, password);
                    editor.apply();

                    Toast.makeText(UserNameSignUp.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
                    // Navigate to AccountCreated activity
                    startActivity(new Intent(UserNameSignUp.this, AccountCreated.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    setInProgress(false);
                    Toast.makeText(UserNameSignUp.this, "Error saving user details", Toast.LENGTH_SHORT).show();
                });
    }

    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            finishButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            finishButton.setVisibility(View.VISIBLE);
        }
    }
}
