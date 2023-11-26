package com.example.jaadu;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskExecution {
    private Context context;
    public String vName;
    private Responcegeneration res = new Responcegeneration(this.context);

    public TaskExecution(Context context) {
        this.context = context;
    }

    @SuppressLint("ShowToast")
    public void performTasks(String task) {
        task = task.toLowerCase();
        if (task.contains("play")) {
            playVideo(task);
        }
        if (task.contains("open youtube")) {
            openYouTube();
        }
        if (task.contains("open snapchat")) {
            openSnap();
        }
        if (task.contains("open instagram")) {
            openInsta();
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

            List<String> contactNames = fetchAndCleanContactNames();
            res.calling(inputContactName);
            if (findContactName(inputContactName, contactNames)) {
                call(inputContactName);
            } else {
                Toast.makeText(context, "Contact not found: " + inputContactName, Toast.LENGTH_SHORT).show();
            }
        }
        if (task.contains("open your settings")) {
            Intent intent = new Intent(context, SettingsActivity.class);
            context.startActivity(intent);
        }
        if (task.contains("set timer for")) {
            String[] words = task.split(" ");
            int minutes = extractMinutes(words);

            // Start the timer
            startTimer(minutes);
        }
        if (task.contains("set alarm for")) {
            int[] time = extractHourAndMinutes(task);

            if (time != null && time.length == 2) {
                setAlarm(time[0], time[1]);
            } else {
                Toast.makeText(context, "Invalid time format", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int[] extractHourAndMinutes(String command) {
        int[] time = new int[2];

        String[] words = command.split(" ");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("for")) {
                if (i + 1 < words.length) {
                    try {
                        // Extract hour and minutes from the format "hh:mm"
                        String[] timeParts = words[i + 1].split(":");
                        if (timeParts.length == 2) {
                            time[0] = Integer.parseInt(timeParts[0]);
                            time[1] = Integer.parseInt(timeParts[1]);
                            return time;
                        }
                    } catch (NumberFormatException e) {
                        // Handle the case where parsing fails
                    }
                }
                break;
            }
        }
        return null;
    }
    private int extractMinutes(String[] words) {
        int minutes = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("for")) {
                if (i + 1 < words.length) {
                    try {
                        minutes = Integer.parseInt(words[i + 1]);
                    } catch (NumberFormatException e) {
                        // Handle the case where parsing minutes fails
                    }
                }
                break;
            }
        }
        return minutes;
    }
    private void startTimer(int minutes) {
        try {
            Intent timerIntent = new Intent(AlarmClock.ACTION_SET_TIMER);

            timerIntent.putExtra(AlarmClock.EXTRA_LENGTH, minutes * 60);

            timerIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Timer message");
            timerIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, false);

            context.startActivity(timerIntent);
        } catch (ActivityNotFoundException e) {

            Toast.makeText(this.context, "Clock app not found or does not support timers", Toast.LENGTH_SHORT).show();
        }
    }
    private void setAlarm(int hour, int minutes) {
        try {
            Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);

            alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, hour);
            alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, minutes);

            alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm message");
            alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, false);

            context.startActivity(alarmIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Clock app not found or does not support alarms", Toast.LENGTH_SHORT).show();
        }
    }
    public void playVideo(String command)
    {
        String vName = command.replace("play", "").trim();
        vName = vName.replace("on youtube", "").trim();
        Toast.makeText(this.context, vName, Toast.LENGTH_SHORT).show();
        new FetchTopVideoTask(this.context,vName).execute();

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

    public String preprocessContactName(String contactName) {
        return contactName.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public boolean findContactName(String inputContactName, List<String> contactNames) {
        String cleanedInputContactName = preprocessContactName(inputContactName);
        for (String cleanedContactName : contactNames) {
            if (cleanedContactName.equals(cleanedInputContactName)) {
                return true;
            }
        }
        return false;
    }

    public void call(String inputContactName) {
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
//                Toast.makeText(context, "Phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();
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
    private void makePhoneCall(String phoneNumber) {
        Toast.makeText(context, "Contact "+phoneNumber+" found", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
    public void openSnap() {
        String spPackage = "com.google.android.snapchat";
        String spUrl = "https://www.snapchat.com/";
        open(spPackage, spUrl);
    }
    public void openInsta() {
        String spPackage = "com.google.android.instagram";
        String spUrl = "https://www.instagram.com/";
        open(spPackage, spUrl);
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

    public boolean isAppInstalled(String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        return intent != null;
    }

}
@SuppressLint("StaticFieldLeak")
class FetchTopVideoTask extends AsyncTask<Void, Void, String>  {

    private final Context context;
    public String vName2 ;
    public FetchTopVideoTask(Context context,String vName) {
        vName2 = vName;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new GsonFactory();

            // Set up the YouTube object to make API requests
            YouTube.Builder builder = new YouTube.Builder(httpTransport, jsonFactory, null);
            YouTube youtube = builder.build();

            // Define the API request parameters
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey("AIzaSyDlXGT-TJd7X2BxOc13mebd0eLp76kDxU0");
            search.setQ(vName2);
            search.setType("video");
            search.setMaxResults(1L);

            // Execute the search request
            SearchListResponse searchResponse = search.execute();

            // Extract the video ID from the search result
            List<SearchResult> searchResults = searchResponse.getItems();
            if (searchResults != null && searchResults.size() > 0) {
                return searchResults.get(0).getId().getVideoId();
            }
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String videoId) {
        if (videoId != null) {
            // Play the topmost video
            Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            context.startActivity(playIntent);
        }
    }
}
