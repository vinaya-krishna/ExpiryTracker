package com.cs646.expirytracker.helper;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static final String EXTRA_ITEM_NAME = "com.cs646.expirytracker.EXTRA_ITEM_NAME";
    public static final String EXTRA_ITEM_COUNT = "com.cs646.expirytracker.EXTRA_ITEM_COUNT";
    public static final String EXTRA_ITEM_DATE = "com.cs646.expirytracker.EXTRA_ITEM_DATE";

    public static final String EXTRA_TRACK_ITEM = "com.cs646.expirytracker.EXTRA_TRACK_ITEM";

    public static final int ADD_ITEM_REQ = 1;
    public static final int EDIT_ITEM_REQ = 2;

    public static void showMessage(Context context, String message){
        Toast.makeText(context, message,Toast.LENGTH_LONG).show();
    }

    public static Date getDateFromString(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getStringFromDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int getNumberofDays(Date date_start, Date date_end){
        long diff = date_end.getTime() - date_start.getTime();
        float days = (diff / (1000*60*60*24));
        return (int)(days);
    }


    //Todo camera integration, save, update database
    //Todo Add calender input
    //Todo camera storage, link update/path
    //Todo add remainder : update view , database, set notification
    //Todo Speech input
    //Todo Update view in list view circular thing and date style
    //Todo different colors for the theme
    //Todo icon for app
    //Todo icon Inside tool bar

}
