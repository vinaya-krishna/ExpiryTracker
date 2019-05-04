package com.cs646.expirytracker.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


/*
Create room database for storage, declare all the data items.
 */

@Entity(tableName = "track_item_table")
@TypeConverters({DateConverter.class})
public class TrackItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private Date dateCreation;

    private Date dateExpiry;

    private int itemCount;


    public TrackItem(String name, Date dateCreation, Date dateExpiry, int itemCount) {
        this.name = name;
        this.dateCreation = dateCreation;
        this.dateExpiry = dateExpiry;
        this.itemCount = itemCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public Date getDateExpiry() {
        return dateExpiry;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDateExpiry(Date dateExpiry) {
        this.dateExpiry = dateExpiry;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setId(int id) {
        this.id = id;
    }
}
