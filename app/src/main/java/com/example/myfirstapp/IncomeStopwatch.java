package com.example.myfirstapp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.SystemClock;

import java.text.DecimalFormat;

public class IncomeStopwatch {

    private static IncomeStopwatch firstInstance = null;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public long Time, StartTime, PauseTime, CurrentTime, OnStopTime, OnStartTime = 0L;

    private Handler handler;

    public boolean bIsRunning;

    private DecimalFormat counterDF = new DecimalFormat("0.0000");

    private float income = 0f;

    /* Private constructor for singleton */
    private IncomeStopwatch(SharedPreferences prefs)
    {
        this.handler = new Handler();
        this.prefs = prefs;
        editor = prefs.edit();
    }

    public static IncomeStopwatch getInstance(SharedPreferences prefs)
    {
        if(firstInstance == null)
        {
            firstInstance = new IncomeStopwatch(prefs);
        }

        return firstInstance;
    }

    public void startStopwatch()
    {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);

        bIsRunning = true;
    }

    public void pauseStopwatch()
    {
        PauseTime += Time;
        handler.removeCallbacks(runnable);

        bIsRunning = false;
    }

    public void resetStopwatch()
    {
        pauseStopwatch();

        Time = 0L ;
        StartTime = 0L ;
        PauseTime = 0L ;
        CurrentTime = 0L ;
        income = 0;
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            updateStopwatch();
            handler.postDelayed(this, 0);
        }

    };

    private void updateStopwatch()
    {
        Time = SystemClock.uptimeMillis() - StartTime;
        CurrentTime = PauseTime + Time;
    }

    public long getCurrentTime()
    {
        return CurrentTime;
    }

    public void onResumeStopwatch()
    {
        OnStartTime = SystemClock.uptimeMillis();
        StartTime = OnStartTime;

        OnStopTime = prefs.getLong("OnStopTime", -1);
        PauseTime = prefs.getLong("PauseTime", -1);
        bIsRunning = prefs.getBoolean("bIsRunning", false);
        if (bIsRunning)
        {
            PauseTime = PauseTime + (OnStartTime - OnStopTime);
            startStopwatch();

        }
    }

    public void onPauseStopwatch()
    {
        if(bIsRunning)
        {
            PauseTime += Time;
        }
        editor.putLong("PauseTime", PauseTime);
        editor.putBoolean("bIsRunning", bIsRunning);
        editor.putLong("OnStopTime", SystemClock.uptimeMillis());

        editor.apply();
    }

}
