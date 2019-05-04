package com.cs646.expirytracker.database;


/*
Room will generate code for each interface
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackItemDao {

    @Insert
    void insert(TrackItem tItem);

    @Update
    void update(TrackItem tItem);

    @Delete
    void delete(TrackItem tItem);

    @Query("SELECT * FROM  track_item_table ORDER BY dateExpiry ASC")
    LiveData<List<TrackItem>> getAllItems();

}
