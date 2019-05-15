package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.helper.Helper;
import com.cs646.expirytracker.helper.TimePicker;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    RelativeLayout notificationTime;
    TextView notificationTimeText;
    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationTime = findViewById(R.id.notification_time);
        notificationTimeText = findViewById(R.id.notification_time_text);

        ArrayList<Integer> timeData = getSharedPreference();

        DecimalFormat formatter = new DecimalFormat("00");
        int hour = timeData.get(0);
        int min = timeData.get(1);

        if(hour > 12)
            notificationTimeText.setText(formatter.format(hour%12) + ":" + formatter.format(min) + " PM");
        else
            notificationTimeText.setText(formatter.format(hour) + ":" + formatter.format(min) + " AM");

        notificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePickerFragment = new TimePicker();
                timePickerFragment.show(getSupportFragmentManager(), "Time Picker");
            }
        });

    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        DecimalFormat formatter = new DecimalFormat("00");
        if(hourOfDay > 12)
            notificationTimeText.setText(formatter.format(hourOfDay%12) + ":" + formatter.format(minute) + " PM");
        else
            notificationTimeText.setText(formatter.format(hourOfDay) + ":" + formatter.format(minute) + " AM");

        saveSharedPreference(hourOfDay, minute);

    }


    private void saveSharedPreference(int hourOfDay, int minute){

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        mEditor.putInt(Helper.HOUR,hourOfDay);
        mEditor.putInt(Helper.MINUTE, minute);
        mEditor.commit();

    }

    private ArrayList<Integer> getSharedPreference(){
        ArrayList<Integer> timeData = new ArrayList<>();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int hour = mPreferences.getInt(Helper.HOUR,9);
        int min = mPreferences.getInt(Helper.MINUTE,0);
        timeData.add(hour);
        timeData.add(min);
        return timeData;
    }


}
