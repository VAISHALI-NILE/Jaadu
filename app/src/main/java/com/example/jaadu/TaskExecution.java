package com.example.jaadu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TaskExecution {
    private Context context;
    private Responcegeneration res = new Responcegeneration();

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
        if (task.contains("open calendar")) {
            openCalender();
        }
        if (task.contains("open calculator")) {
            openCalculator();
        }

        if (task.contains("open contacts")) {
            openContacts();
        }
        if (task.contains("open email")) {
            openEmails();
        }
        if (task.contains("open music")) {
            openMusic();
        }
        if (task.contains("open messages")) {
            openMessages();
        }
        if (task.contains("open maps")) {
            openMaps();
        }
        if (task.contains("open photos") || task.contains("open gallery")) {
            openGallery();
        }


        if (task.contains("call")) {

            String inputContactName = task.replace("call", "").trim().toLowerCase();
            res.calling(null,inputContactName);

            // Fetch and clean contact names
            List<String> contactNames = fetchAndCleanContactNames();

            // Compare the cleaned input contact name with cleaned contact names
            if (findContactName(inputContactName, contactNames)) {
                res.calling(inputContactName,null);
                call(inputContactName);
            } else {
                Toast.makeText(context, "Contact not found: " + inputContactName, Toast.LENGTH_SHORT).show();
            }
        }
        if(task.contains("open your settings"))
        {
            Intent intent = new Intent(context,SettingsActivity.class);
            context.startActivity(intent);
        }
    }


    public void openSpotify() {
        String spPackage = "com.google.android.spotify";
        String spUrl = "https://www.spotify.com/";
        open(spPackage, spUrl);
    }
    private void openCalculator() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_CALCULATOR);
        context.startActivity(intent);
    }

    private void openContacts() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_CONTACTS);
        context.startActivity(intent);
    }
    private void openEmails() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        context.startActivity(intent);
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_GALLERY);
        context.startActivity(intent);
    }
    private void openMessages() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
        context.startActivity(intent);
    }
    private void openMusic() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MUSIC);
        context.startActivity(intent);
    }
    private void openMaps() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MAPS);
        context.startActivity(intent);
    }

    private void openCalender() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_CALENDAR);
        context.startActivity(intent);
    }
    public void openYouTube() {
        String youtubePackage = "com.google.android.youtube";
        String youtubeUrl = "https://www.youtube.com/";
        open(youtubePackage, youtubeUrl);
    }



    public void open(String packageName, String url) {
        if (isAppInstalled(packageName)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setPackage(packageName);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        }
    }


    public List<String> fetchAndCleanContactNames() {
        List<String> cleanedContactNames = new ArrayList<>();

        String[] projection = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String sortOrder = null;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String cleanedContactName = preprocessContactName(contactName);
                cleanedContactNames.add(cleanedContactName);
                Log.d("ContactNames", cleanedContactName);
            }

            cursor.close();
        }

        return cleanedContactNames;
    }

    private String preprocessContactName(String contactName) {
        if (contactName == null) {
            return "";
        }

        // Remove symbols and emojis
        String cleanedName = contactName.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Convert to lowercase
        cleanedName = cleanedName.toLowerCase();

        return cleanedName;
    }

    public boolean findContactName(String inputContactName, @NonNull List<String> contactNames) {
        // Clean the input contact name
        String cleanedInputContactName = preprocessContactName(inputContactName);

        // Compare the cleaned input contact name with cleaned contact names (case-insensitive)
        for (String cleanedContactName : contactNames) {
            if (cleanedContactName.equals(cleanedInputContactName)) {
                return true;
            }
        }
        return false;
    }

    public void call(String inputContactName) {
        // Retrieve the original contact name from the contact list
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {inputContactName};
        String sortOrder = null;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                sortOrder
        );

        if (cursor != null && cursor.moveToFirst()) {
            int phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = cursor.getString(phoneNumberColumnIndex);
            cursor.close();

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Toast.makeText(context, "Phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();
                call2(phoneNumber);
            } else {
                Toast.makeText(context, "Phone number not found for " + inputContactName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Contact not found: " + inputContactName, Toast.LENGTH_SHORT).show();
        }
    }

    public void call2(String phoneNumber)
    {

        Uri number = Uri.parse("tel:" + phoneNumber);
        Toast.makeText(context, "Contact "+phoneNumber+" found", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        context.startActivity(callIntent);
    }
    public boolean isAppInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return intent != null;
    }

    private String getContactPhoneNumber(String contactName) {

        return null;
    }
}
