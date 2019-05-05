package com.cs646.expirytracker.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;
import com.cs646.expirytracker.viewmodel.TrackItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private TrackItemViewModel trackItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        FloatingActionButton buttonAddItem = findViewById(R.id.button_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemListActivity.this, EditItemActivity.class);
                startActivityForResult(intent, Helper.REQUEST_ADD_ITEM);
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


        /*
        Handle Swipe on the list item
        Delete on left swipe
         */

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    trackItemViewModel.deleteItem(itemAdapter.getTrackItemAt(viewHolder.getAdapterPosition()));
                    Helper.showMessage(getBaseContext(), "Item Deleted");
            }
        }).attachToRecyclerView(recyclerView);


        /*
        Handle Click on the list item
         */

        itemAdapter.setOnItemListener(new ItemAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ItemListActivity.this, ViewItemActivity.class);
                intent.putExtra(Helper.EXTRA_TRACK_ITEM,itemAdapter.getTrackItemAt(position));
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Helper.REQUEST_ADD_ITEM && resultCode == RESULT_OK){
            TrackItem trackItem = data.getParcelableExtra(Helper.EXTRA_TRACK_ITEM);
            trackItemViewModel.insertItem(trackItem);
        }

    }

}
