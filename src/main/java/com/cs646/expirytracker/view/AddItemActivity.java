package com.cs646.expirytracker.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.cs646.expirytracker.R;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
//        setTitle("Add Item");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.add_item_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save_item:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
