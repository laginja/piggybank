package com.example.myfirstapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "stopwatch_table")
public class Stopwatch {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long stopwatchValue;

    private float incomeValue;

    private Date date;

    public Stopwatch(long stopwatchValue, float incomeValue, Date date) {
        this.stopwatchValue = stopwatchValue;
        this.incomeValue = incomeValue;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getStopwatchValue() {
        return stopwatchValue;
    }

    public float getIncomeValue() {
        return incomeValue;
    }

    public Date getDate() { return date; }
}
