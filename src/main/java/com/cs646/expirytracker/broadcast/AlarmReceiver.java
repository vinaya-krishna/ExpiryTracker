package com.cs646.expirytracker.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notifierKey = intent.getIntExtra(NOTIFICATION_ID, 0);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        notificationManager.notify(notifierKey, notification);
    }
}
