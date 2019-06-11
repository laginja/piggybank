package com.example.myfirstapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "income_table")
public class Income {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private float value;

    private Date date;

    public Income(float value, Date date) {
        this.value = value;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
