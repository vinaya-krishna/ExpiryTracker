package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.cs646.expirytracker.R;

public class ViewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);


        Toolbar toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

    }


}
