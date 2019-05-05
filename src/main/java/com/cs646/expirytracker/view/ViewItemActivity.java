package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class ViewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final TrackItem trackItem = intent.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);

        TextView itemCount = findViewById(R.id.view_item_count);
        itemCount.setText(""+trackItem.getItemCount());

        toolbar.setTitle(trackItem.getName());

        TextView itemAddedOn = findViewById(R.id.view_add_date);
        itemAddedOn.setText(Helper.getStringFromDate(trackItem.getDateCreation()));

        TextView itemExpiryOn = findViewById(R.id.view_expiry_date);
        itemExpiryOn.setText(Helper.getStringFromDate(trackItem.getDateExpiry()));

        TextView itemDaysLeft = findViewById(R.id.view_days_left);

        int num_of_days = Helper.getNumberofDays(new Date(), trackItem.getDateExpiry());
        itemDaysLeft.setText(""+num_of_days);


        FloatingActionButton edit_track_item = findViewById(R.id.button_edit_item);
        edit_track_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ViewItemActivity.this, EditItemActivity.class);
                editIntent.putExtra(Helper.EXTRA_TRACK_ITEM,trackItem);
                startActivity(editIntent);
            }
        });

    }


}
