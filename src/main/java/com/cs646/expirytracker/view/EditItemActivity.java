package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.helper.Helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_NAME = "com.cs646.expirytracker.EXTRA_ITEM_NAME";
    public static final String EXTRA_ITEM_COUNT = "com.cs646.expirytracker.EXTRA_ITEM_COUNT";
    public static final String EXTRA_ITEM_DATE = "com.cs646.expirytracker.EXTRA_ITEM_DATE";

    private EditText mItemName;
    private EditText mItemCount;
    private TextView mItemExpiryDate;
    private FloatingActionButton mSaveItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        mItemName = findViewById(R.id.edit_item_name);
        mItemCount = findViewById(R.id.edit_item_count);
        mItemExpiryDate = findViewById(R.id.edit_item_expiry_date);
        mSaveItem = findViewById(R.id.button_save_item);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        setTitle("Add Item");


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
        Intent data = new Intent();
        data.putExtra(EXTRA_ITEM_NAME,itemName);
        data.putExtra(EXTRA_ITEM_COUNT,itemCount);
        data.putExtra(EXTRA_ITEM_DATE,itemExpiryDate);
        setResult(RESULT_OK, data);
        finish();
    }
}
