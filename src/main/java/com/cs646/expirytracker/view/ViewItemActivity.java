package com.cs646.expirytracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;
import com.cs646.expirytracker.viewmodel.TrackItemViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Date;

public class ViewItemActivity extends AppCompatActivity {

    TextView itemAddedOn, itemExpiryOn, itemDaysLeft, itemReminderDate;
    Toolbar toolbar;
    TrackItem trackItem;
    ImageView viewItemPhoto;
    LinearLayout itemDayLeftLayout, itemExpiredLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        toolbar = findViewById(R.id.view_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        trackItem = intent.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);

        itemAddedOn = findViewById(R.id.view_add_date);
        itemExpiryOn = findViewById(R.id.view_expiry_date);
        itemDaysLeft = findViewById(R.id.view_days_left);
        itemDayLeftLayout = findViewById(R.id.layout_daysleft);
        itemExpiredLayout = findViewById(R.id.layout_expired);
        viewItemPhoto = findViewById(R.id.view_item_photo);
        itemReminderDate = findViewById(R.id.view_item_reminder_date);

        updateView(trackItem);

        FloatingActionButton edit_track_item = findViewById(R.id.button_edit_item);
        edit_track_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ViewItemActivity.this, EditItemActivity.class);
                editIntent.putExtra(Helper.EXTRA_TRACK_ITEM,trackItem);
//                startActivityForResult(editIntent, Helper.REQUEST_EDIT_ITEM);
                startActivity(editIntent);
            }
        });

    }

    private void updateView(TrackItem trackItem){
        Glide.with(this).load(new File(trackItem.getItemImagePath())).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_image_placeholder)).into(viewItemPhoto);
        itemReminderDate.setText(Helper.getStringFromDate(trackItem.getDateReminder()));
        CollapsingToolbarLayout toolbar = findViewById(R.id.view_collapsing_toolbar);
        toolbar.setTitle(trackItem.getName());

        itemAddedOn.setText(Helper.getStringFromDate(trackItem.getDateCreation()));
        itemExpiryOn.setText(Helper.getStringFromDate(trackItem.getDateExpiry()));
        int num_of_days = Helper.getNumberofDays(new Date(), trackItem.getDateExpiry());
        itemDaysLeft.setText(""+num_of_days);
        if(num_of_days <= 0){
            itemDayLeftLayout.setVisibility(View.GONE);
            itemExpiredLayout.setVisibility(View.VISIBLE);
        }
        else {
            itemDayLeftLayout.setVisibility(View.VISIBLE);
            itemExpiredLayout.setVisibility(View.GONE);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == Helper.REQUEST_EDIT_ITEM && resultCode == RESULT_OK){
//            trackItem = data.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);
//            //Update the view
//            trackItemViewModel.updateItem(trackItem);
//            //Update in the ViewModel
//            updateView(trackItem);
//        }
//    }
}
