package com.example.techtangle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtangle.utils.AndroidUtil;
import com.example.techtangle.utils.NetworkUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private TextView nameTextView;
    private TextView usernameTextView;
    private SwitchCompat notificationsSwitch, publicAccountSwitch;
    private Button logoutButton;
    private AppCompatButton GoTo;

    private String username;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String NOTIFICATIONS_MUTED_KEY = "notificationsMuted";
    private static final String PUBLIC_ACCOUNT_KEY = "publicAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        usernameTextView = findViewById(R.id.usernameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        publicAccountSwitch = findViewById(R.id.publicAccountSwitch);
        logoutButton = findViewById(R.id.logoutButton);
        GoTo = findViewById(R.id.GoTo);

        username = AndroidUtil.getUsername(this);

        if (username == null || username.isEmpty()) {
            Toast.makeText(Settings.this, "Username not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(Settings.this, IntroWelcome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        GoTo.setOnClickListener(v -> {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
            finish();
        });

        loadCachedUserData();
        loadNotificationSettings();
        loadPublicAccountSettings();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATIONS_MUTED_KEY, isChecked);
            editor.apply();
            Toast.makeText(Settings.this, "Notifications " + (isChecked ? "muted" : "unmuted"), Toast.LENGTH_SHORT).show();
        });

        publicAccountSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updatePublicAccountSetting(!isChecked); // Reverse the logic here
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PUBLIC_ACCOUNT_KEY, isChecked);
            editor.apply();
        });

        if (NetworkUtil.isNetworkConnected(this)) {
            fetchUserData();
        } else {
            Toast.makeText(Settings.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCachedUserData() {
        String cachedName = AndroidUtil.getCachedname(this);
        String cachedUsername = AndroidUtil.getCachedUsername(this);

        if (cachedName != null) {
            nameTextView.setText(cachedName);
        }
        if (cachedUsername != null) {
            usernameTextView.setText(cachedUsername);
        }
    }

    private void fetchUserData() {
        db.collection("users").document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String fetchedName = document.getString("name");
                            Boolean isPublicAccount = document.getBoolean("publicAccount");

                            nameTextView.setText(fetchedName);
                            usernameTextView.setText(username);

                            // Reverse the switch state based on publicAccount value from Firestore
                            publicAccountSwitch.setChecked(isPublicAccount == null || !isPublicAccount);

                            AndroidUtil.cacheUsername(this, username);
                        } else {
                            handleUserNotFound();
                        }
                    } else {
                        handleFetchUserDataError(task.getException());
                    }
                });
    }

    private void handleUserNotFound() {
        nameTextView.setText("User not found");
        usernameTextView.setText("Unknown");
        Toast.makeText(Settings.this, "User not found in the database", Toast.LENGTH_SHORT).show();
    }

    private void handleFetchUserDataError(Exception e) {
        Toast.makeText(Settings.this, "Error getting user document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void loadNotificationSettings() {
        boolean notificationsMuted = sharedPreferences.getBoolean(NOTIFICATIONS_MUTED_KEY, false);
        notificationsSwitch.setChecked(notificationsMuted);
    }

    private void loadPublicAccountSettings() {
        boolean isPublicAccount = sharedPreferences.getBoolean(PUBLIC_ACCOUNT_KEY, false);
        publicAccountSwitch.setChecked(!isPublicAccount); // Reverse the logic here
    }

    private void updatePublicAccountSetting(boolean shouldBePublic) {
        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentSnapshot snapshot = transaction.get(db.collection("users").document(username));
                    transaction.update(snapshot.getReference(), "publicAccount", shouldBePublic);
                    return null;
                }).addOnSuccessListener(aVoid -> Toast.makeText(Settings.this, "Account privacy updated", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Settings.this, "Error updating account privacy: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
