package com.example.jaadu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String profileImageUrl = account.getPhotoUrl().toString();
        String name = account.getDisplayName();

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

        // Find the sign-out button
        Button signOutButton = findViewById(R.id.signOutButton);

        // Set onClickListener to the sign-out button
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        Button terms = findViewById(R.id.TandC);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, terms_and_consitions.class);
                startActivity(i);
            }
        });


        Button about = findViewById(R.id.aboutus);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, about_us.class);
                startActivity(i);
            }
        });
    }
    // Function to handle sign-out
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        // Redirect to the GoogleAuthentication or another appropriate screen after sign-out
        // For example, go back to the login screen
        Intent intent = new Intent(SettingsActivity.this, GoogleAuthentication.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity to prevent going back with back button
    }
}