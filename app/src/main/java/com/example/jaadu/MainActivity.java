package com.example.jaadu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int CALL_PERMISSION_REQUEST_CODE = 2;
    private TextSwitcher textSwitcher;

    private ImageView micIV;
    private TextView userInput;
//    private TextView jaaduResponse;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String WELCOME_MESSAGE_DISPLAYED_KEY = "welcomeMessageDisplayed";

    private TextToSpeech textToSpeech;
    private GifDrawable gif;
    String response ;
    private VideoView bgVideo;
    private TaskExecution tsk = new TaskExecution(this);
    private Responcegeneration rsp = new Responcegeneration(this);
    public static boolean calling_flag = false;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSwitcher = findViewById(R.id.jaaduResponce);
        textSwitcher.setFactory(() -> {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(22);
            return textView;});

        ImageView prf = findViewById(R.id.profile);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        String profileImageUrl = account.getPhotoUrl().toString();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean welcomeMessageDisplayed = settings.getBoolean(WELCOME_MESSAGE_DISPLAYED_KEY, false);

        Glide.with(this)
                .load(profileImageUrl)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.profile_placeholder) // Optional placeholder image while loading
                .into(prf);


        prf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        micIV = findViewById(R.id.mic_speak_iv);

        micIV.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.mic_disabled_color));

//        jaaduResponse = findViewById(R.id.jaaduResponce);

//        ObjectAnimator animator = ObjectAnimator.ofFloat(jaaduResponse, "translationX", -500f, 0f);
//        animator.setDuration(2000);

        micIV.setOnClickListener(view -> {
            listen();
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                        String welcomeMessage = "Welcome Human!";
                        textToSpeech.speak(welcomeMessage, TextToSpeech.QUEUE_FLUSH, null);
//                        jaaduResponse.setText(welcomeMessage);
                        textSwitcher.setText(welcomeMessage);
//                        ObjectAnimator animator = ObjectAnimator.ofFloat(jaaduResponse, "translationX", -500f, 0f);
//                        animator.setDuration(2000);
//                        animator.start();

                }
            }
        });
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(WELCOME_MESSAGE_DISPLAYED_KEY, true);
        editor.apply();
    }
    public Void listen()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAudioPermission();
        }
        micIV.setColorFilter(ContextCompat.getColor(this, R.color.mic_enabled_color)); // #FF0E87E7
        startSpeechToText();
        return null;
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
                    tsk.performTasks(result.get(0));
                    response = rsp.responce(result.get(0));
                    textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null);
                    String[] res = response.split("");
                    if(result.get(0).contains("bye"))
                    {
                        System.exit(0);
                    }

//                    textSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in));
//                    textSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out));

                    final int[] index = {0};
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (index[0] < response.length()) {
                                String partialText = response.substring(0, index[0] + 1);
                                textSwitcher.setText(partialText);
                                index[0]++;
                                handler.postDelayed(this, 100); // Typing speed (adjust as needed)
                            }
                        }
                    };

                    handler.post(runnable);
//                    jaaduResponse.setText(response);
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(jaaduResponse, "translationX", -500f, 0f);
//                    animator.setDuration(2000);
//                    animator.start();
//                    jaaduResponse.animate().alpha(1f).setDuration(1500);


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
            } else {
                Toast.makeText(this, "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
