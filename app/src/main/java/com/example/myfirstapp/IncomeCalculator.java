package com.example.myfirstapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class IncomeCalculator {

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String INCOME = "income";

    private static IncomeCalculator firstInstance = null;

    private SharedPreferences sharedPreferences;

    Calendar c = Calendar.getInstance();   // this takes current date
    Date firstDayOfMonth;
    private float incomeLong;
    private long nowMilis;
    private long milisFromBegginingMonth;
    // Number of days in the current month
    private int numDaysInCurrentMonth;
    private float increment;
    private float incrementMS;
    private float startingIncome;

    public IncomeCalculator(Context context)
    {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        setup();
    }

    private void setup()
    {
        setupData();
        setupCalendar();
    }

    private void setupData() {
        incomeLong = sharedPreferences.getFloat(INCOME, 0);
        Log.e("incomeLong: ", "incomeLong: " + incomeLong);
    }

    public void setupCalendar() {
        // Get first day of current month
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        firstDayOfMonth = c.getTime();

        // Get number of days in current month
        numDaysInCurrentMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private void calculateStartingIncome() {
        // Current time in MS
        nowMilis = System.currentTimeMillis();

        // MS passed from the beginning of the month until now
        milisFromBegginingMonth = nowMilis - c.getTimeInMillis();

        // Always re-calculate starting income when the activity resumes
        startingIncome = milisFromBegginingMonth * getPerSecond() / 1000;
        increment = getPerSecond() / 10;
    }

    public float getCurrentIncome()
    {
        calculateStartingIncome();

        startingIncome += increment;

        return startingIncome;
    }

    public float getIncrementMS()
    {
        incrementMS = getPerSecond() / 1000;

        return incrementMS;
    }

    private float getPerDay()
    {
        return incomeLong / numDaysInCurrentMonth;
    }

    private float getPerHour()
    {
        return getPerDay() / 24;
    }

    private float getPerMinute()
    {
        return getPerHour() / 60;
    }

    private float getPerSecond()
    {
        return getPerMinute() / 60;
    }

}
