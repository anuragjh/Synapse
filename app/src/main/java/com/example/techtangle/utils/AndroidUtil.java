package com.example.techtangle.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AndroidUtil {

    private static final String SHARED_PREFS = "my_shared_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String PREFERENCES_FILE = "user_data";
    private static final String KEY_OCCUPATION = "occupation";

    // Static variable to hold the username in memory
    private static String username;

    // Method to set the username
    public static void setUsername(Context context, String user) {
        username = user;
        saveUsernameToPrefs(context, user);
    }

    // Method to get the username
    public static String getUsername(Context context) {
        if (username == null) {
            username = getUsernameFromPrefs(context);
        }
        return username;
    }

    // Save username to SharedPreferences
    private static void saveUsernameToPrefs(Context context, String user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user);
        editor.apply();
    }

    // Retrieve username from SharedPreferences
    private static String getUsernameFromPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
    public static String getCachedUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "User not found");
    }

    public static String getCachedOccupation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return prefs.getString(KEY_OCCUPATION, "Unknown");
    }

    public static void cacheUserData(Context context, String username, String occupation) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_OCCUPATION, occupation);
        editor.apply();
    }
}
