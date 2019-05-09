package com.cs646.expirytracker.helper;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.broadcast.AlarmReceiver;
import com.cs646.expirytracker.database.TrackItem;

import java.util.Calendar;
import java.util.Date;

public class NotificationScheduler {
    private static NotificationScheduler instance;
    private Context context;
    private Class target;

    public  NotificationScheduler(Context context, Class target){
        this.context = context;
        this.target = target;
    }

    private Notification getNotification(TrackItem trackItem, String title, String text){

        PendingIntent pendingIntent = getPendingIntent(trackItem);

        Notification notification = new NotificationCompat.Builder(context, AppNotification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_app_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        return  notification;

    }

    private PendingIntent getPendingIntent(TrackItem trackItem){
        Intent targetIntent = new Intent(context, target);
        targetIntent.putExtra(Helper.EXTRA_TRACK_ITEM,trackItem);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(targetIntent);
        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }

    private long getTimeToNotify(Date reminderDate){
        //todo implement getTimeToNotify and  date convertion

        Calendar cal = Calendar.getInstance();
        cal.setTime(reminderDate);
        cal.set(Calendar.HOUR_OF_DAY,9);
        reminderDate = cal.getTime();

        return reminderDate.getTime();

    }

    private void deleteNotification(TrackItem trackItem){
        //todo implement deleteNotification
    }


    public void scheduleNotification(TrackItem trackItem){

        String title = trackItem.getName();
        String text = trackItem.getName() + " expiring on " + trackItem.getDateExpiry();

        long futureInMillis = getTimeToNotify(trackItem.getDateReminder());

        Intent broadCastIntent = new Intent(context, AlarmReceiver.class);
        broadCastIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, trackItem.getId());
        broadCastIntent.putExtra(AlarmReceiver.NOTIFICATION,getNotification(trackItem, title, text));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trackItem.getId(), broadCastIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //action on time
//        long futureInMillis = SystemClock.elapsedRealtime() + timeForNotify;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

    }


}
