package com.example.jaadu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_PERMISSION_REQUEST_CODE = 2;

    private ImageView micIV;
    private TextView userInput;
    private TextView jaaduResponse;

    private TextToSpeech textToSpeech;
    private GifDrawable gif;
    private TaskExecution tsk = new TaskExecution(this);
    private Responcegeneration rsp = new Responcegeneration();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micIV = findViewById(R.id.mic_speak_iv);

        micIV.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.mic_disabled_color));
        jaaduResponse = new TextView(MainActivity.this);
        userInput = findViewById(R.id.userInput);
        jaaduResponse = findViewById(R.id.jaaduResponce);



        micIV.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkAudioPermission();
            }
            // changing the color of the mic icon, which indicates that it is currently listening
            micIV.setColorFilter(ContextCompat.getColor(this, R.color.mic_enabled_color)); // #FF0E87E7
            startSpeechToText();
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // If no error is found, set the language of speech to UK
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);

                }
            }
        });
    }

    private void startSpeechToText() {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
                // changing the color of the mic icon to gray to indicate it is not listening
                micIV.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.mic_disabled_color)); // #FF6D6A6A
            }

            @Override
            public void onError(int i) {
            }


            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (result != null) {

                    //userInput.setText(result.get(0));

                    String response = rsp.responce(result.get(0));
                    textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null);
                    jaaduResponse.setAlpha(0f);
                    jaaduResponse.setText(response);
                    jaaduResponse.animate().alpha(1f).setDuration(1500);

                    tsk.performTasks(result.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });

        speechRecognizer.startListening(speechRecognizerIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAudioPermission() {
        int microphonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (microphonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
        }

        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Microphone permission granted, you can now use the microphone
                // If needed, you can add more code here to handle the permission result
            } else {
                // Microphone permission denied, you can inform the user or take necessary action
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Call permission granted, you can now make calls
                // If needed, you can add more code here to handle the permission result
            } else {
                // Call permission denied, you can inform the user or take necessary action
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
