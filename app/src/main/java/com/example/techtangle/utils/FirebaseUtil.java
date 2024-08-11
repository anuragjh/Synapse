package com.example.techtangle.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

//    public static String currentUserId() {
//        if (auth.getCurrentUser() != null) {
//            return auth.getCurrentUser().getUid();
//        } else {
//            return null;
//        }
//    }
//
//    public static DocumentReference currentUserDetails() {
//        String userId = currentUserId();
//        if (userId != null) {
//            return db.collection("users").document("userId");
//        } else {
//            // Log or handle the case where userId is null
//            return null;
//        }
//    }
}
