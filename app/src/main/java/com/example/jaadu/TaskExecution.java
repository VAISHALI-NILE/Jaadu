package com.example.jaadu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

public class TaskExecution {
    private Context context;

    public TaskExecution(Context context) {
        this.context = context;
    }

    public void performTasks(String task) {
        task = task.toLowerCase();
        if (task.contains("open youtube")) {
            openYouTube();
        }
        if (task.contains("open spotify")) {
            openSpotify();
        }
        if(task.contains("open calendar"))
        {
            openCalender();
        }
        if (task.contains("call")) {
            String contactName = task.replace("call", "").trim();
            call(contactName);
        }
    }

    public void call(String contactName) {
        // Perform a contact lookup to get the phone number for the contact
        String phoneNumber = getContactPhoneNumber(contactName);

        if (phoneNumber != null) {
            // Initiate the call
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
            context.startActivity(callIntent);
        } else {
            Toast.makeText(context, "Contact "+contactName+" not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSpotify() {
        String spPackage = "com.google.android.spotify";
        String spUrl = "https://www.spotify.com/";
        open(spPackage, spUrl);
    }

    public void openYouTube() {
        String youtubePackage = "com.google.android.youtube";
        String youtubeUrl = "https://www.youtube.com/";
        open(youtubePackage, youtubeUrl);
    }

    public void openCalender()
    {
        String calenderPackage ="com.android.calendar";
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(calenderPackage);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }


    public void open(String packageName, String url) {
        if (isAppInstalled(packageName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage(packageName);
            context.startActivity(intent);
        } else {
            // If the app is not installed, open in a web browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
    }

    public boolean isAppInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    // Implement your contact lookup logic here
    private String getContactPhoneNumber(String contactName) {
        // You should implement a method to look up the contact and retrieve the phone number
        // This could involve querying the device's contact list or a custom database
        return null;
    }
}
