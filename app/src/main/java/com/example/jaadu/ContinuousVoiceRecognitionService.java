package com.example.jaadu;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class ContinuousVoiceRecognitionService extends Service {

    private static final int NOTIFICATION_ID = 1234;
    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //startForegroundWithNotification();
        initializeSpeechRecognizer();
        startListening(); // This is where the service starts listening for voice commands
        return START_STICKY;
    }

//    private void startForegroundWithNotification() {
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//        Notification notification = new NotificationCompat.Builder(this, "ChannelID")
//                .setContentTitle("Voice Recognition Service")
//                .setContentText("Listening for voice commands")
//                .setSmallIcon(R.drawable.googleimg)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        startForeground(NOTIFICATION_ID, notification);
//    }

    private void initializeSpeechRecognizer() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Handle permissions if not granted
            System.out.println("HIIIIIIIII");
            return;
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {}

            @Override
            public void onError(int error) {}

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && matches.size() > 0) {
                    String command = matches.get(0); // Retrieve the recognized voice command
                    if (command.contains("open app")) { // Check for a specific voice command
                        // Open your app here
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.jaadu");
                        if (launchIntent != null) {
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);
                        }
                    }
                }
                // Start listening again after processing the previous result
                startListening();
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    private void startListening() {
        if (speechRecognizer != null) {
            speechRecognizer.startListening(recognizerIntent); // Initiates voice recognition
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}