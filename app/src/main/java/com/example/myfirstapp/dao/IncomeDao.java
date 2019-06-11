package com.example.myfirstapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myfirstapp.model.Income;

import java.util.List;

// Needs to be an interface because we don't provide method bodies, only
// definitions which we annotate.
// Usually create 1 DAO per entity
@Dao
public interface IncomeDao {

    @Insert
    void insert(Income income);

    @Update
    void update(Income income);

    @Delete
    void delete(Income income);

    @Query("DELETE FROM income_table")
    void deleteAllIncomes();

    @Query("SELECT * FROM income_table ORDER BY date DESC")
    LiveData<List<Income>> getAllIncomes();     // LiveData - changes made to income_table will be seen immediately

    @Query("SELECT SUM(value) FROM income_table")
    LiveData<Double> getIncomesSum();           // Alternative to LiveData would be this method: double getIncomesSumFloat();
                                                // The problem is that the latter method doesn't update data live so when we add something to the DB and try to get it instantly
                                                // it wouldn't work cause the viewmodel hasn't been updated. LiveData can be observed and it updated whenever it's changed.

    @Query("SELECT SUM(value) FROM income_table")
    float getIncomesSumFloat();
}
