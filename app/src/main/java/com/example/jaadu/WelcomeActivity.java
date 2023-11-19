package com.example.jaadu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        System.out.println("In Welcome");
        ScreenReceiver screenReceiver = new ScreenReceiver(); // Initialize the ScreenReceiver

        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT); // Use ACTION_USER_PRESENT
        registerReceiver(screenReceiver, filter);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        con = findViewById(R.id.continueButton);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });

    }
    private void openMain(){
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);
    }
}