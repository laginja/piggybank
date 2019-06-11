package com.example.myfirstapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.myfirstapp.model.Tag;

import java.util.List;

@Dao
public interface TagDao {

    @Insert
    void insert(Tag tag);

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);

    @Query("SELECT * FROM tag_table ORDER BY dateAdded DESC")
    LiveData<List<Tag>> getAllTagsByDateAdded();     // LiveData - changes made to tag_table will be seen immediately

    @Query("SELECT * FROM tag_table ORDER BY timeClicked DESC")
    LiveData<List<Tag>> getAllTagsByTimeClicked();     // LiveData - changes made to tag_table will be seen immediately

}
