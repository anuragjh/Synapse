package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class PasswordSignUp extends AppCompatActivity {

    private EditText passwordEditText, confirmPasswordEditText;
    private Button nextButton;
    private ImageView passwordVisibilityIcon;
    private ImageView confirmPasswordVisibilityIcon;
    private String email, name, dob, profession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_and_password_sign_up);

        // Initialize views
        passwordEditText = findViewById(R.id.dobEditText);
        confirmPasswordEditText = findViewById(R.id.customProfessionEditText);
        nextButton = findViewById(R.id.nextButton);
        passwordVisibilityIcon = findViewById(R.id.passwordVisibilityIcon);
        confirmPasswordVisibilityIcon = findViewById(R.id.passwordVisibilityIcon2);

        // Set up text watchers for icons
        setupTextWatcher(passwordEditText, findViewById(R.id.passwordicon));
        setupTextWatcher(confirmPasswordEditText, findViewById(R.id.confirmPasswordicon));

        // Retrieve previous extras
        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            email = incomingIntent.getStringExtra("email");
            name = incomingIntent.getStringExtra("name");
            dob = incomingIntent.getStringExtra("dob");
            profession = incomingIntent.getStringExtra("profession");
        }
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        // Set up visibility toggling
        setupPasswordVisibilityToggle(passwordEditText, passwordVisibilityIcon);
        setupPasswordVisibilityToggle(confirmPasswordEditText, confirmPasswordVisibilityIcon);

        // Handle next button click
        nextButton.setOnClickListener(v -> {
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (password.isEmpty()) {
                passwordEditText.setError("Enter Password");
            } else if (confirmPassword.isEmpty()) {
                confirmPasswordEditText.setError("Confirm Password");
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(PasswordSignUp.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                // Pass all data to the next activity
                Intent intent = new Intent(PasswordSignUp.this, UserNameSignUp.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("dob", dob);
                intent.putExtra("profession", profession);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }

    private void setupTextWatcher(EditText editText, ImageView icon) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int color = s.toString().isEmpty() ?
                        R.color.colorSecondaryText : R.color.colorPrimaryVariant;
                icon.setColorFilter(ContextCompat.getColor(PasswordSignUp.this, color));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupPasswordVisibilityToggle(EditText editText, ImageView visibilityIcon) {
        visibilityIcon.setOnClickListener(v -> {
            if (editText.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // Show password
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                visibilityIcon.setImageResource(R.drawable.visibility_visible);
            } else {
                // Hide password
                editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                visibilityIcon.setImageResource(R.drawable.visibility_gone);
            }
            editText.setSelection(editText.getText().length());
        });
    }
}
