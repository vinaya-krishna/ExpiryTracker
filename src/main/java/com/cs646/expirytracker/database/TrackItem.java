package com.cs646.expirytracker.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;


/*
Create room database for storage, declare all the data items.
 */

@Entity(tableName = "track_item_table")
@TypeConverters({DateConverter.class})
public class TrackItem implements Parcelable {

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

    protected TrackItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
        itemCount = in.readInt();
        dateCreation = new Date(in.readLong());
        dateExpiry = new Date(in.readLong());
    }

    public static final Creator<TrackItem> CREATOR = new Creator<TrackItem>() {
        @Override
        public TrackItem createFromParcel(Parcel in) {
            return new TrackItem(in);
        }

        @Override
        public TrackItem[] newArray(int size) {
            return new TrackItem[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(itemCount);
        dest.writeLong(dateCreation.getTime());
        dest.writeLong(dateExpiry.getTime());
    }
}
