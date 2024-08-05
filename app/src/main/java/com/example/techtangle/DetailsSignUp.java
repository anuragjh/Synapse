package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class DetailsSignUp extends AppCompatActivity {

    private EditText nameEditText, dobEditText, customProfessionEditText;
    private Button nextButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_sign_up);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryVariant));

        // Initialize views
        nameEditText = findViewById(R.id.nameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        customProfessionEditText = findViewById(R.id.customProfessionEditText);
        nextButton = findViewById(R.id.nextButton);
        ImageView nameIcon = findViewById(R.id.nameIcon);
        ImageView dobicon = findViewById(R.id.dobicon);
        ImageView professionicon = findViewById(R.id.professionicon);

        // Set icon color based on text input
        setupTextWatcher(nameEditText, nameIcon);
        setupTextWatcher(dobEditText, dobicon);
        setupTextWatcher(customProfessionEditText, professionicon);

        // Retrieve email from the previous intent
        Intent incomingIntent = getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra("email")) {
            email = incomingIntent.getStringExtra("email");
        }

        // Handle next button click
        nextButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String dob = dobEditText.getText().toString().trim();
            String profession = customProfessionEditText.getText().toString().trim();

            if (name.isEmpty()) {
                nameEditText.setError("Enter Name!!");
            } else if (dob.isEmpty()) {
                dobEditText.setError("Enter Date!!");
            } else if (!isValidYear(dob)) {
                dobEditText.setError("Enter a valid year (1930 - current year)!!");
            } else if (profession.isEmpty()) {
                customProfessionEditText.setError("Enter Profession");
            } else {
                // Pass email to the next activity
                Intent intent = new Intent(DetailsSignUp.this, PasswordSignUp.class);
                intent.putExtra("name", name);
                intent.putExtra("dob", dob);
                intent.putExtra("profession", profession);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // Back button handling (if needed)
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupTextWatcher(EditText editText, ImageView icon) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int color = s.toString().isEmpty() ?
                        R.color.colorSecondaryText : R.color.colorPrimaryVariant;
                icon.setColorFilter(ContextCompat.getColor(DetailsSignUp.this, color));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean isValidYear(String yearText) {
        try {
            int year = Integer.parseInt(yearText);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            return year >= 1930 && year <= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
