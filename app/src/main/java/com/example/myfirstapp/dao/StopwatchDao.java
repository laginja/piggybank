package com.example.myfirstapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.myfirstapp.model.Stopwatch;

import java.util.List;

@Dao
public interface StopwatchDao {

    @Insert
    void insert(Stopwatch stopwatch);

    @Delete
    void delete(Stopwatch stopwatch);

    @Query("DELETE FROM stopwatch_table")
    void deleteAllStopwatches();

    @Query("SELECT * FROM stopwatch_table ORDER BY date DESC")
    LiveData<List<Stopwatch>> getAllStopwatches();     // LiveData - changes made to income_table will be seen immediately
}
