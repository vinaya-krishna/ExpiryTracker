package com.cs646.expirytracker.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.viewmodel.TrackItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    public static final int ADD_ITEM_REQ = 1;

    private TrackItemViewModel trackItemViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        FloatingActionButton buttonAddItem = findViewById(R.id.button_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemListActivity.this,EditItemActivity.class);
                startActivityForResult(intent, ADD_ITEM_REQ);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ItemAdapter itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);

        trackItemViewModel = ViewModelProviders.of(this).get(TrackItemViewModel.class);
        trackItemViewModel.getAllItems().observe(this, new Observer<List<TrackItem>>() {
            @Override
            public void onChanged(List<TrackItem> trackItems) {
               itemAdapter.setItems(trackItems);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_ITEM_REQ && resultCode == RESULT_OK){
            //do something get data
        }
    }
}
