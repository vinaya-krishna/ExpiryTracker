package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.helper.TimePicker;

import java.text.DecimalFormat;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    RelativeLayout notificationTime;
    TextView notificationTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        notificationTime = findViewById(R.id.notification_time);
        notificationTimeText = findViewById(R.id.notification_time_text);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int hour = sharedPref.getInt("hour", 9);
        int min = sharedPref.getInt("minute", 0);
        DecimalFormat formatter = new DecimalFormat("00");
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

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.commit();

    }

}
