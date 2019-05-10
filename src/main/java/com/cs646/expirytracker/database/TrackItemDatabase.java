package com.cs646.expirytracker.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.helper.Helper;

import java.util.Date;

@Database(entities = {TrackItem.class}, version = 5)
public abstract class TrackItemDatabase extends RoomDatabase {

    private static TrackItemDatabase instance;

    /*
    Must contain an abstract method that returns object of the DAO
    */
    public abstract TrackItemDao trackItemDao();

    public static synchronized TrackItemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                                            TrackItemDatabase.class,
                                            "track_item_database"
                                            )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();

        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{

        private TrackItemDao trackItemDao;

        private PopulateDBAsyncTask(TrackItemDatabase trackItemDatabase){
            this.trackItemDao = trackItemDatabase.trackItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            this.trackItemDao.insert( new TrackItem("Bread",new Date(), new Date(), 10, Helper.getURLForResource(R.drawable.firework)));
//            this.trackItemDao.insert( new TrackItem("Apple",new Date(), new Date(), 14, Helper.getURLForResource(R.drawable.firework)));
//            this.trackItemDao.insert( new TrackItem("Banana",new Date(), new Date(), 11, Helper.getURLForResource(R.drawable.firework)));
            return null;
        }
    }

}
