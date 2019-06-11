package com.example.myfirstapp.notification;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.myfirstapp.IncomeCalculator;
import com.example.myfirstapp.R;

public class AlertReceiver extends BroadcastReceiver {

    private NotificationManagerCompat notificationManager;

    private String title = "Piggybank";

    IncomeCalculator incomeCalculator;

    // poziva se kad se alarm ukljuci
    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = NotificationManagerCompat.from(context);

        incomeCalculator = new IncomeCalculator(context);

        Notification notification = new NotificationCompat.Builder(context, PiggybankApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText("So far you've earned " + incomeCalculator.getCurrentIncome())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(2, notification);
    }
}
