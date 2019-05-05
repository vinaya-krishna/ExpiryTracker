package com.cs646.expirytracker.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<TrackItem> items = new ArrayList<>();
    private OnItemListener mOnItemListener;


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_item, parent, false);
        return new ItemHolder(itemView, mOnItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        TrackItem trackItem = items.get(position);
        holder.textViewName.setText(trackItem.getName());
        String date_n = Helper.getStringFromDate(trackItem.getDateExpiry());

        Date todayDate = new Date();
        long diff = trackItem.getDateExpiry().getTime() - todayDate.getTime();
        float days = (diff / (1000*60*60*24));

        holder.textViewDate.setText(date_n);

        holder.textViewReaminingDays.setText((int)days + " days remaining");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setItems(List<TrackItem> trackItems){
        this.items = trackItems;
        //TODO change later
        notifyDataSetChanged();
    }

    public void setmOnItemListener(OnItemListener onItemListener){
        mOnItemListener = onItemListener;
    }


    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewName;
        private TextView textViewDate;
        private TextView textViewReaminingDays;
        private OnItemListener onItemListener;

        public ItemHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            this.onItemListener = onItemListener;
            textViewName = itemView.findViewById(R.id.item_name);
            textViewDate = itemView.findViewById(R.id.item_expiry_date);
            textViewReaminingDays = itemView.findViewById(R.id.item_days_remain);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }


}