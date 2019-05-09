package com.cs646.expirytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.cs646.expirytracker.view.ItemListActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EasySplashScreen splashConfig = new EasySplashScreen(SplashActivity.this)
                .withFullScreen()
                .withSplashTimeOut(2000)
                .withTargetActivity(ItemListActivity.class)
                .withBackgroundColor(Color.parseColor("#00838F"))
                .withLogo(R.mipmap.ic_launcher_round);

        View splshView = splashConfig.create();
        setContentView(splshView);
    }
}
