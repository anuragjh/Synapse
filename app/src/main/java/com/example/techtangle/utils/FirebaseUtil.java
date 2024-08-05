package com.example.techtangle.utils;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseUtil {

    // Check if a user is logged in
    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

}
