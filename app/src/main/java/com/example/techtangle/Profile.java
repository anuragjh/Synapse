package com.example.techtangle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techtangle.model.UserModel;
import com.example.techtangle.utils.AndroidUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView profileImage;
    private TextView profileName, followingCount, followingLabel, fansCount, fansLabel, likesCount, postsLabel;
    private Button followButton, chatButton;
    private LinearLayout statsLayout;

    private UserModel currentUserModel;
    private DocumentReference currentUserDetails;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username; // This should be set to the username for fetching data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize all views
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        followingCount = findViewById(R.id.followingCount);
        followingLabel = findViewById(R.id.followingLabel);
        fansCount = findViewById(R.id.fansCount);
        fansLabel = findViewById(R.id.fansLabel);
        likesCount = findViewById(R.id.likesCount);
        postsLabel = findViewById(R.id.postsLabel);
        followButton = findViewById(R.id.followButton);
        chatButton = findViewById(R.id.chatButton);
        statsLayout = findViewById(R.id.statsLayout);

        username = AndroidUtil.getUsername(this);

        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
            return;
        }

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        fetchUserProfile(username);

        followButton.setOnClickListener(v -> {
            DocumentReference userDocRef = db.collection("users").document(username);

            db.runTransaction(transaction -> {
                DocumentSnapshot snapshot = transaction.get(userDocRef);
                if (!snapshot.exists()) {
                    throw new IllegalStateException("User does not exist");
                }

                // Get the current following list
                List<String> followingList = (List<String>) snapshot.get("following");
                if (followingList == null) {
                    followingList = new ArrayList<>();
                }

                if (followButton.getText().toString().equalsIgnoreCase("unfollow")) {
                    // If the user is following, remove them from the list
                    if (followingList.contains(username)) {
                        followingList.remove(username);
                        transaction.update(userDocRef, "following", followingList);
                        followButton.setText("Follow");
                    }
                } else {
                    // If the user is not following, add them to the list
                    if (!followingList.contains(username)) {
                        followingList.add(username);
                        transaction.update(userDocRef, "following", followingList);
                        followButton.setText("Unfollow");
                    }
                }

                return null;
            }).addOnSuccessListener(aVoid -> {
                fetchUserProfile(username);
            }).addOnFailureListener(e -> Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });


    }

    private void fetchUserProfile(String username) {
        DocumentReference userDocRef = db.collection("users").document(username);
        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    profileName.setText(document.getString("name"));

                    // Fetch and set followers, fans, and posts count
                    Long followingCountValue = (long) (document.get("following") != null ? ((List<?>) document.get("following")).size() : 0);
                    Long fansCountValue = document.getLong("fansCount");
                    Long postsCountValue = document.getLong("postsCount");

                    followingCount.setText(String.valueOf(followingCountValue != null ? followingCountValue : 0));
                    fansCount.setText(String.valueOf(fansCountValue != null ? fansCountValue : 0));
                    likesCount.setText(String.valueOf(postsCountValue != null ? postsCountValue : 0));

                    // Check if the current user is already following
                    List<String> followingList = (List<String>) document.get("following");
                    if (followingList != null && followingList.contains(username)) {
                        followButton.setText("Unfollow");
                    } else {
                        followButton.setText("Follow");
                    }

                } else {
                    profileName.setText("User not found");
                    followingCount.setText("0");
                    fansCount.setText("0");
                    likesCount.setText("0");
                }
            } else {
                profileName.setText("Error fetching data");
                followingCount.setText("0");
                fansCount.setText("0");
                likesCount.setText("0");
            }
        });
    }



    private void incrementFollowingCount(String username) {
        DocumentReference userDocRef = db.collection("users").document(username);
        userDocRef.update("followingCount", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    // Update UI after successful update
                    fetchUserProfile(username); // Refresh the profile data
                    Toast.makeText(Profile.this, "Following count updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure
                    Toast.makeText(Profile.this, "Error updating following count: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
