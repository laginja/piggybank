package com.example.myfirstapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tag_table")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private boolean isActive;

    private Date dateAdded;

    private Date timeClicked;

    //private float income;

    public Tag(String name, boolean isActive, Date dateAdded, Date timeClicked) {
        this.name = name;
        this.isActive = isActive;
        this.dateAdded = dateAdded;
        this.timeClicked = timeClicked;
        //this.income = income;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getTimeClicked() {
        return timeClicked;
    }

   /* public float getIncome() {
        return income;
    }*/
}
