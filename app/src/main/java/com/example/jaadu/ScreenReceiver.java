package com.example.jaadu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver {
    private static final String TAG = "ScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()))
        {
            Intent serviceIntent = new Intent(context, ContinuousVoiceRecognitionService.class);
            context.startService(serviceIntent);
            // Device is unlocked; perform your desired action here
            Log.d(TAG, "Device unlocked");
            // You can trigger any action or start your service here
        }
    }
}


