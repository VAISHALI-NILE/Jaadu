package com.example.jaadu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String profileImageUrl = account.getPhotoUrl().toString();
        String name = account.getDisplayName();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Assuming you have the profile image URL in a variable named profileImageUrl
        ImageView profileImageView = findViewById(R.id.profileImageView); // Replace with your ImageView ID
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        Glide.with(this)
                .load(profileImageUrl)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.profile_placeholder) // Placeholder image while loading (optional)
                .into(profileImageView);

        usernameTextView.setText(name);
    }


}