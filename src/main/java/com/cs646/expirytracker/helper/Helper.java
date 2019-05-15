package com.cs646.expirytracker.helper;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.cs646.expirytracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {

    public static final String EXTRA_ITEM_NAME = "com.cs646.expirytracker.EXTRA_ITEM_NAME";
    public static final String EXTRA_ITEM_COUNT = "com.cs646.expirytracker.EXTRA_ITEM_COUNT";
    public static final String EXTRA_ITEM_DATE = "com.cs646.expirytracker.EXTRA_ITEM_DATE";

    public static final String EXTRA_TRACK_ITEM = "com.cs646.expirytracker.EXTRA_TRACK_ITEM";


    public static final int REQUEST_ADD_ITEM = 1;
    public static final int REQUEST_EDIT_ITEM = 2;
    public static final int REQUEST_TAKE_PHOTO = 3;
    public static final int REQUEST_GALLERY_PHOTO = 4;
    public static final int REQUEST_SPEECH_INPUT_ITEM_NAME = 5;
    public static final int REQUEST_SPEECH_INPUT_EXPIRY_DATE = 6;
    public static final int REQUEST_SPEECH_INPUT_REMINDER_DATE = 7;

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

    public static Date setTime(Date date, int hour, int min, int sec){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND,sec);
        cal.set(Calendar.MILLISECOND,00);
        date = cal.getTime();
        System.out.println("SET : "+date);
        return date;
    }

    public static String getStringFromDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int getNumberofDays(Date date_start, Date date_end){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date_start);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        date_start = cal.getTime();
        long diff = date_end.getTime() - date_start.getTime();
        double days = (diff / (1000*60*60*23.9));
        return (int)(days);
    }

    public static String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" +resourceId).toString();
    }


    //todo sync the settigs time with notification time
    //todo replace all strings with values
    //todo save notification time in the preference, by default 9 am
    //todo replace strings in java files getString(R.string.saved_high_score_key)

}
