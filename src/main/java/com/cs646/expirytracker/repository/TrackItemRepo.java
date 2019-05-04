package com.cs646.expirytracker.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.database.TrackItemDao;
import com.cs646.expirytracker.database.TrackItemDatabase;

import java.util.List;

public class TrackItemRepo {
    private TrackItemDao trackItemDao;
    private LiveData<List<TrackItem>> allTrackItems;

    public TrackItemRepo(Application application){
        TrackItemDatabase trackItemDatabase = TrackItemDatabase.getInstance(application);
        trackItemDao = trackItemDatabase.trackItemDao();
        allTrackItems = trackItemDao.getAllItems();

    }

    public void insert(TrackItem trackItem){
        new InsertItemAsyncTask(trackItemDao).execute(trackItem);
    }

    public void update(TrackItem trackItem){
        new UpdateItemAsyncTask(trackItemDao).execute(trackItem);
    }

    public void delete(TrackItem trackItem){
        new DeleteItemAsyncTask(trackItemDao).execute(trackItem);
    }

    public LiveData<List<TrackItem>> getAllTrackItems(){
        return allTrackItems;
    }


    private static class InsertItemAsyncTask extends AsyncTask<TrackItem, Void, Void>{

        private TrackItemDao trackItemDao;

        private InsertItemAsyncTask(TrackItemDao trackItemDao){
            this.trackItemDao = trackItemDao;
        }

        @Override
        protected Void doInBackground(TrackItem... trackItems) {
            this.trackItemDao.insert(trackItems[0]);
            return null;
        }
    }


    private static class UpdateItemAsyncTask extends AsyncTask<TrackItem, Void, Void>{

        private TrackItemDao trackItemDao;

        private UpdateItemAsyncTask(TrackItemDao trackItemDao){
            this.trackItemDao = trackItemDao;
        }

        @Override
        protected Void doInBackground(TrackItem... trackItems) {
            this.trackItemDao.update(trackItems[0]);
            return null;
        }
    }


    private static class DeleteItemAsyncTask extends AsyncTask<TrackItem, Void, Void>{

        private TrackItemDao trackItemDao;

        private DeleteItemAsyncTask(TrackItemDao trackItemDao){
            this.trackItemDao = trackItemDao;
        }

        @Override
        protected Void doInBackground(TrackItem... trackItems) {
            this.trackItemDao.delete(trackItems[0]);
            return null;
        }
    }


}
