package com.example.myfirstapp.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myfirstapp.notification.AlertReceiver;
import com.example.myfirstapp.R;
import com.example.myfirstapp.fragment.TimePickerFragment;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener {
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";

    private Spinner spinner;
    private Switch aSwitch;

    private boolean switchChecked;

    private TextView notificationTime;
    private Button notificationTimeBtn;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private int hour;
    private int minute;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    // we have to use 2 different notification managers
    // this one is for showing notifications and checks for backwards compatibility
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Preferences");

        spinner = findViewById(R.id.currencySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        aSwitch = findViewById(R.id.enableNotificationsSwitch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Log.e("Switch: ", "Is true");
                    notificationTime.setVisibility(View.VISIBLE);
                    notificationTimeBtn.setVisibility(View.VISIBLE);

                    prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, prefs.getInt("HourOfDay", 0));
                    c.set(Calendar.MINUTE, prefs.getInt("MinuteOfDay", 0));
                    c.set(Calendar.SECOND, 0);

                    startAlarm(c);
                } else
                {
                    Log.e("Switch: ", "Is false");
                    notificationTime.setVisibility(View.INVISIBLE);
                    notificationTimeBtn.setVisibility(View.INVISIBLE);

                    stopRepeatingAlarm();
                }

                switchChecked = isChecked;
            }
        });

        notificationTime = findViewById(R.id.notificationTimeTextView);
        notificationTimeBtn = findViewById(R.id.timePickerBtn);
        notificationTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        notificationManager = NotificationManagerCompat.from(this);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
        Log.d("TimePicker: ", "Hour: " + hourOfDay + " Minute: " + minuteOfDay);

        hour = hourOfDay;
        minute = minuteOfDay;
        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = prefs.edit();

        editor.putInt("HourOfDay", hour);
        editor.putInt("MinuteOfDay", minute);

        editor.apply();

        TextView notificationTime = findViewById(R.id.notificationTimeTextView);
        notificationTime.setText(String.format("%02d:%02d", prefs.getInt("HourOfDay" , 1), prefs.getInt("MinuteOfDay",1)));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        startAlarm(c);
    }

    private void startAlarm(Calendar c) {

        long timerMilis = c.getTimeInMillis();
        long nowMilis = System.currentTimeMillis();

        if(timerMilis <= nowMilis)
        {
            // set timer for the next day
            timerMilis += AlarmManager.INTERVAL_DAY;
        }

        long milisPerDay = 1000 * 60 * 60 * 24;
        long milisPerMin = 1000 * 60;

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timerMilis, milisPerDay, pendingIntent);
    }

    private void stopRepeatingAlarm()
    {
        alarmManager.cancel(pendingIntent);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = prefs.edit();

        int spinnerSelectionPosition = spinner.getSelectedItemPosition();
        String currency = spinner.getSelectedItem().toString();

        editor.putInt("SpinnerSelectionPosition", spinnerSelectionPosition);
        editor.putBoolean("SwitchChecked", switchChecked);
        editor.putInt("HourOfDay", hour);
        editor.putInt("MinuteOfDay", minute);

        switch (currency){
            case "USD":
                editor.putString("Currency", "$");
                break;
            case "EUR":
                editor.putString("Currency", "€");
                break;
            case "HRK":
                editor.putString("Currency", "HRK");
                break;
            default:
                editor.putString("Currency", "$");
        }


        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        spinner.setSelection(prefs.getInt("SpinnerSelectionPosition", 0));
        aSwitch.setChecked(prefs.getBoolean("SwitchChecked", false));

        hour = prefs.getInt("HourOfDay" , 0);
        minute = prefs.getInt("MinuteOfDay",0);
        notificationTime.setText(String.format("%02d:%02d", hour, minute));
    }
}
