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

    public static String getStringFromDate(Date date){
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(Calendar.HOUR_OF_DAY,9);
//        date = cal.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int getNumberofDays(Date date_start, Date date_end){
        long diff = date_end.getTime() - date_start.getTime();
        float days = (diff / (1000*60*60*24));
        return (int)(days);
    }

    public static String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+ R.class.getPackage().getName()+"/" +resourceId).toString();
    }









    //Todo Update view in list view circular thing and date style

    //Todo different colors for the theme
    //Todo icon for app
    //Todo icon Inside tool bar

    //todo show expired today, expires tomorrow, expires on Date
    //todo notification , show proper date
    //todo if possible implemt settings always notification comes at 9am

    //todo
    //correction in view page
    //view -> edit section
    // reminder date fill

    //todo using calender you can add substract date https://stackoverflow.com/questions/5894726/how-do-i-do-calendar-arithmetic-with-java-util-date

}
