package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;
import com.cs646.expirytracker.viewmodel.TrackItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditItemActivity extends AppCompatActivity {

    private EditText mItemName;
    private EditText mItemCount;
    private TextView mItemExpiryDate;
    private FloatingActionButton mSaveItem;
    private TrackItemViewModel trackItemViewModel;
    private boolean EDIT_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        trackItemViewModel = ViewModelProviders.of(this).get(TrackItemViewModel.class);

        mItemName = findViewById(R.id.edit_item_name);
        mItemCount = findViewById(R.id.edit_item_count);
        mItemExpiryDate = findViewById(R.id.edit_item_expiry_date);
        mSaveItem = findViewById(R.id.button_save_item);



        Intent intent = getIntent();
        final TrackItem trackItem = intent.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);

        if(intent.hasExtra(Helper.EXTRA_TRACK_ITEM)){
            toolbar.setTitle("Edit Item");
            EDIT_MODE = true;
            mItemName.setText(trackItem.getName());
            mItemCount.setText(""+trackItem.getItemCount());
            mItemExpiryDate.setText(Helper.getStringFromDate(trackItem.getDateExpiry()));
        }else{
            toolbar.setTitle("Add Item");
            EDIT_MODE = false;
        }




        mSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });



    }

    private void saveItem(){
        String itemName = mItemName.getText().toString().trim();
        String itemCount = mItemCount.getText().toString().trim();
        String itemExpiryDate = mItemExpiryDate.getText().toString().trim();

        if(itemName.isEmpty() || itemCount.isEmpty() || itemExpiryDate.isEmpty()){
            Helper.showMessage(this, "Please Enter All details");
            return;
        }

        if(EDIT_MODE){
            TrackItem updatedTrackItem = getIntent().getParcelableExtra(Helper.EXTRA_TRACK_ITEM);
            updatedTrackItem.setName(itemName);
            updatedTrackItem.setDateExpiry(Helper.getDateFromString(itemExpiryDate));
            updatedTrackItem.setItemCount(Integer.parseInt(itemCount));
            trackItemViewModel.updateItem(updatedTrackItem);
        }
        else {
            Intent data = new Intent();
            data.putExtra(Helper.EXTRA_ITEM_NAME,itemName);
            data.putExtra(Helper.EXTRA_ITEM_COUNT,itemCount);
            data.putExtra(Helper.EXTRA_ITEM_DATE,itemExpiryDate);
            setResult(RESULT_OK, data);
        }
        finish();
    }
}
