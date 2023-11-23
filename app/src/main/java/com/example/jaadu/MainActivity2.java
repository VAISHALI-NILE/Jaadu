package com.example.jaadu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String FIRST_TIME_KEY = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isFirstTime = settings.getBoolean(FIRST_TIME_KEY, true);

        if (isFirstTime) {
            // Perform actions for the first time
            startActivity(new Intent(this, WelcomeActivity.class));

            // Save the first-time flag
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(FIRST_TIME_KEY, false);
            editor.apply();
        } else {
            // Perform actions for subsequent launches
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}