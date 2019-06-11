package com.example.myfirstapp.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class PiggybankApp extends Application {
    // Wraps out whole application with it's activities etc.

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        Log.d("Build versions:", Build.VERSION.SDK_INT + "" );
        Log.d("Build versions:", Build.VERSION_CODES.O + "" );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,                                   // channel ID
                    "Channel 1",                              // channel NAME
                    NotificationManager.IMPORTANCE_HIGH           // importance level
            );
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,                                   // channel ID
                    "Channel 2",                              // channel NAME
                    NotificationManager.IMPORTANCE_LOW             // importance level
            );
            channel2.setDescription("This is Channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
        else
        {
            Log.d("Aplication:", "Notification channels not supported");
        }

    }
}
