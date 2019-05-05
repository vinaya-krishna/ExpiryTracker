package com.cs646.expirytracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.repository.TrackItemRepo;

import java.util.List;

public class TrackItemViewModel extends AndroidViewModel {

    private TrackItemRepo trackItemRepo;
    private LiveData<List<TrackItem>> allTrackItems;


    public TrackItemViewModel(@NonNull Application application) {
        super(application);
        trackItemRepo = new TrackItemRepo(application);
    }

    public void insertItem(TrackItem trackItem){
        trackItemRepo.insert(trackItem);
    }
    public void updateItem(TrackItem trackItem){
        trackItemRepo.update(trackItem);
    }
    public void deleteItem(TrackItem trackItem){
        trackItemRepo.delete(trackItem);
    }

    public LiveData<List<TrackItem>> getAllItems(){
        allTrackItems = trackItemRepo.getAllTrackItems();
        return allTrackItems;
    }

}
