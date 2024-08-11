package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techtangle.Adapter.OptionAdapter;
import com.example.techtangle.utils.AndroidUtil;
import com.example.techtangle.utils.NetworkUtil;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView occupationTextView;
    private String username;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ImageView settings_button,profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        usernameTextView = findViewById(R.id.username);
        occupationTextView = findViewById(R.id.occupation);
        recyclerView = findViewById(R.id.recycler_view);
        settings_button = findViewById(R.id.settings_button);
        profile_image= findViewById(R.id.profile_image);

        username = AndroidUtil.getUsername(this);

        if (username == null || username.isEmpty()) {
            Toast.makeText(MainActivity.this, "Username not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        // Load cached user data
        loadCachedUserData();

        settings_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        });
        profile_image.setOnClickListener(v -> {
            Intent intent = new Intent(this, Profile.class);
            startActivity(intent);
        });

        if (NetworkUtil.isNetworkConnected(this)) {
            // Fetch and update user data from Firestore
            fetchUserData();
        } else {
            Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        // Initialize RecyclerView
//        List<String> options = Arrays.asList("Trending", "Gen Z", "NEWz", "Coding Points", "Abstract", "Cityscape", "Portraits", "Macro", "Events", "Black & White");
//        OptionAdapter optionAdapter = new OptionAdapter(options);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(optionAdapter);
    }

    private void loadCachedUserData() {
        String cachedUsername = AndroidUtil.getCachedname(this);
        String cachedOccupation = AndroidUtil.getCachedOccupation(this);

        if (cachedUsername != null) {
            usernameTextView.setText(cachedUsername);
        }
        if (cachedOccupation != null) {
            occupationTextView.setText(cachedOccupation);
        }
    }

    private void fetchUserData() {
        db.collection("users").document(username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            String fetchedUsername = document.getString("name");
                            String fetchedOccupation = document.getString("profession");

                            // Update UI with fetched data
                            usernameTextView.setText(fetchedUsername);
                            occupationTextView.setText(fetchedOccupation);

                            // Cache the fetched data
                            AndroidUtil.cacheUserData(this, fetchedUsername, fetchedOccupation);
                        } else {
                            // Handle case when document doesn't exist
                            usernameTextView.setText("User not found");
                            occupationTextView.setText("Unknown");
                        }
                    } else {
                        // Handle failure
                        Toast.makeText(MainActivity.this, "Error getting user document.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
