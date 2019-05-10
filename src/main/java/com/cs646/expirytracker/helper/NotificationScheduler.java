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
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {
    private static NotificationScheduler instance;
    private Context context;
    private Class target;
    private AlarmManager alarmManager;


    public static NotificationScheduler getInstance(){
        if(instance == null)
            instance = new NotificationScheduler();
        return instance;
    }

    private NotificationScheduler(){
    }

    public void initScheduler(Context context, Class target){
        this.context = context;
        this.target = target;
        this.alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
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
//        long diffMillSec;
//        Date dateNow = new Date();
//        diffMillSec = reminderDate.getTime() - dateNow.getTime();
//        System.out.println(diffMillSec);
//        System.out.println(TimeUnit.MILLISECONDS.toDays(Math.abs(diffMillSec)));
//        if(diffMillSec <= 0){
//            if(TimeUnit.MILLISECONDS.toDays(Math.abs(diffMillSec)) == 0)
//                return 2000;
//            else
//                return -1;
//        }
//        return diffMillSec;
        return 20000;

    }

    public void deleteNotification(TrackItem trackItem){
        PendingIntent alarmReceiverPendingIntent = getAlarmReceiverPendingIntent(trackItem, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println(alarmReceiverPendingIntent);
        alarmManager.cancel(alarmReceiverPendingIntent);
        System.out.println("Alarm canceled");
    }


    private String getNotifyText(TrackItem trackItem){
        int noOfDays = Helper.getNumberofDays(new Date(), trackItem.getDateExpiry());
        String expityDate = Helper.getStringFromDate(trackItem.getDateExpiry());
        String notifyText = trackItem.getName() + " expiring on " + expityDate;
        String day;
        switch (noOfDays){
            case 0:
                day = " expired Yesterday";
                notifyText = trackItem.getName() + day;
                break;
            case 1:
                day = " expiring Today";
                notifyText = trackItem.getName() + day;
                break;
            case 2:
                day = " expiring Tomorrow";
                notifyText = trackItem.getName() + day;
                break;
        }
        if(noOfDays < 0){
            day = " expired on "+ expityDate;
            notifyText = trackItem.getName() + day;
        }

        return notifyText;
    }

    private PendingIntent getAlarmReceiverPendingIntent(TrackItem trackItem, int flag){
        System.out.println(context);
        System.out.println(trackItem.getId());

        String title = trackItem.getName();
        String text = getNotifyText(trackItem);
        Intent broadCastIntent = new Intent(context, AlarmReceiver.class);
        broadCastIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, trackItem.getId());
        broadCastIntent.putExtra(AlarmReceiver.NOTIFICATION,getNotification(trackItem, title, text));
        return PendingIntent.getBroadcast(context, trackItem.getId(), broadCastIntent, flag);
    }

    public void scheduleNotification(TrackItem trackItem){

        long timeForNotify = getTimeToNotify(trackItem.getDateReminder());
        //set notification only if product reminder is same day or later days
        if(timeForNotify != -1){
            System.out.println("Scheduled on "+ timeForNotify);
            long futureInMillis = SystemClock.elapsedRealtime() + timeForNotify;
            PendingIntent alarmReceiverPendingIntent = getAlarmReceiverPendingIntent(trackItem, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, alarmReceiverPendingIntent);
        }
        else{
            System.out.println("Alarm not scheduled");
        }
    }


}
