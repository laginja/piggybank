package com.example.myfirstapp.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.IncomeCalculator;
import com.example.myfirstapp.R;
import com.example.myfirstapp.model.Stopwatch;
import com.example.myfirstapp.viewmodel.StopwatchViewModel;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AddStopwatchActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedPrefs";

    private TextView stopwatchTextView;
    private TextView incomeTextView;

    private Button startPauseBtn, resetBtn, addDBBtn;

    private long Time, StartTime, PauseTime, CurrentTime, OnStopTime, OnStartTime = 0L;

    private Handler handler;

    private boolean bIsRunning;

    IncomeCalculator incomeCalculator;

    private DecimalFormat counterDF = new DecimalFormat("0.0000");

    private float income = 0f;

    private StopwatchViewModel stopwatchViewModel;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stopwatch);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Stopwatch");

        stopwatchTextView = (TextView)findViewById(R.id.stopwatchTextView);
        incomeTextView = (TextView)findViewById(R.id.incomeTextView);
        startPauseBtn = (Button)findViewById(R.id.startPauseButton);
        resetBtn = (Button)findViewById(R.id.resetButton);
        resetBtn.setEnabled(false);
        addDBBtn = (Button)findViewById(R.id.buttonAddDB);
        addDBBtn.setEnabled(false);

        handler = new Handler();
        stopwatchViewModel = ViewModelProviders.of(this).get(StopwatchViewModel.class);

        startPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bIsRunning)
                {
                    startStopwatch();
                } else
                {
                    pauseStopwatch();
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetStopwatch();
            }
        });

        addDBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStopwatchDB();
            }
        });
    }

    private void insertStopwatchDB() {
        long stopwatchValue = CurrentTime;
        float incomeValue = income;

        Stopwatch stopwatch = new Stopwatch(stopwatchValue, incomeValue, new Date());
        stopwatchViewModel.insert(stopwatch);
        resetStopwatch();
        Toast.makeText(this, "Stopwatch saved", Toast.LENGTH_SHORT).show();
    }

    private void startStopwatch()
    {
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
        handler.postDelayed(runnable2, 100);

        startPauseBtn.setText("Pause");
        resetBtn.setEnabled(true);
        addDBBtn.setEnabled(false);

        bIsRunning = true;
    }

    private void pauseStopwatch()
    {
        PauseTime += Time;
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(runnable2);

        startPauseBtn.setText("Start");
        addDBBtn.setEnabled(true);

        bIsRunning = false;
    }

    private void resetStopwatch()
    {
        pauseStopwatch();

        Time = 0L ;
        StartTime = 0L ;
        PauseTime = 0L ;
        CurrentTime = 0L ;
        income = 0;

        stopwatchTextView.setText("00:00,00");
        incomeTextView.setText("0.00 " + prefs.getString("Currency", "USD"));

        resetBtn.setEnabled(false);
        addDBBtn.setEnabled(false);
        startPauseBtn.setText("Start");
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            updateStopwatchText();
            handler.postDelayed(this, 0);
        }

    };

    private void updateStopwatchText()
    {
        Time = SystemClock.uptimeMillis() - StartTime;
        CurrentTime = PauseTime + Time;

        stopwatchTextView.setText(String.format("%02d:%02d,%02d",
                TimeUnit.MILLISECONDS.toMinutes(CurrentTime),
                TimeUnit.MILLISECONDS.toSeconds(CurrentTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(CurrentTime)),
                ((TimeUnit.MILLISECONDS.toMillis(CurrentTime) - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(CurrentTime))) / 10) % 100

        ));
    }

    public Runnable runnable2 = new Runnable() {

        public void run() {

            updateIncomeText();
            handler.postDelayed(this, 100);
        }

    };

    private void updateIncomeText()
    {
        income = incomeCalculator.getIncrementMS() * CurrentTime;
        incomeTextView.setText(counterDF.format(income) + " " + prefs.getString("Currency", "USD"));
    }

    // These two methods are called no matter how we leave the activity (if the system destroys it, if we leave over the back button or even if we close the app)
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("StopwatchActivity", "onPause called");

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = prefs.edit();

        if(bIsRunning)
        {
            PauseTime += Time;
        }
        editor.putLong("PauseTime", PauseTime);
        editor.putBoolean("bIsRunning", bIsRunning);
        editor.putBoolean("bIsEnabledReset", resetBtn.isEnabled());
        editor.putBoolean("bIsEnabledAdd", addDBBtn.isEnabled());
        editor.putLong("OnStopTime", SystemClock.uptimeMillis());

        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("StopwatchActivity", "onResume called");

        incomeCalculator = new IncomeCalculator(getApplicationContext());

        OnStartTime = SystemClock.uptimeMillis();
        StartTime = OnStartTime;

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        OnStopTime = prefs.getLong("OnStopTime", -1);
        PauseTime = prefs.getLong("PauseTime", -1);
        bIsRunning = prefs.getBoolean("bIsRunning", false);
        if (bIsRunning)
        {
            Log.e("Stopwatch", "B IS RUNNING");
            PauseTime = PauseTime + (OnStartTime - OnStopTime);
            resetBtn.setEnabled(prefs.getBoolean("bIsEnabledReset", false));
            addDBBtn.setEnabled(prefs.getBoolean("bIsEnabledAdd", false));
        }
        updateStopwatchText();
        updateIncomeText();

        if (bIsRunning)
        {
            startStopwatch();
        } else
        {
            Log.e("Stopwatch", "B IS NOT RUNNING");
            resetBtn.setEnabled(prefs.getBoolean("bIsEnabledReset", false));
            addDBBtn.setEnabled(prefs.getBoolean("bIsEnabledAdd", false));
        }
    }


}
