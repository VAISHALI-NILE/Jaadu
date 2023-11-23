package com.example.jaadu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
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
        if(task.contains("play") )
        {
            playVideo(task);
        }
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
            String contactName = task.replace("call", "").trim();
            call(contactName);
        }
        if(task.contains("open your settings"))
        {
            Intent intent = new Intent(context,SettingsActivity.class);
            context.startActivity(intent);
        }
    }
    public void playVideo(String command)
    {
        String vName = command.replace("play", "").trim();
//        String API_KEY = "AIzaSyDlXGT-TJd7X2BxOc13mebd0eLp76kDxU0";
//        new SearchOnYouTubeTask().execute(vName, API_KEY);
        Toast.makeText(this.context, vName, Toast.LENGTH_SHORT).show();
        new FetchTopVideoTask(this.context,vName).execute();

    }





//    public void playSong(String command)
//    {
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .setJsonMapper(JacksonMapper.create())
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams, connectionListener);
//
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new ColorSpace.Connector.ConnectionListener() {
//
//                    @Override
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        // Now you can use the SpotifyAppRemote API
//                        playTrack("spotify:track:your_track_uri");
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        // Handle connection failure
//                    }
//                });
//    }
//
//    private void playTrack(String trackUri) {
//        mSpotifyAppRemote.getPlayerApi().play(trackUri);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//    }
//}

    public void call(String contactName) {
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {contactName};
        String sortOrder = null;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
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
                Toast.makeText(context, "Phone number not found for " + contactName, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Contact not found: " + contactName, Toast.LENGTH_SHORT).show();
        }
    }

    public void call2(String phoneNumber)
    {

        Uri number = Uri.parse("tel:" + phoneNumber);
        Toast.makeText(context, "Contact "+phoneNumber+" found", Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
        context.startActivity(callIntent);
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
            // Set up the YouTube object to make API requests
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
            // Handle GoogleJsonResponseException, which may contain API error details
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
