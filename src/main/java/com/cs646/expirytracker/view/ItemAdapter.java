package com.cs646.expirytracker.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs646.expirytracker.R;
import com.cs646.expirytracker.database.TrackItem;
import com.cs646.expirytracker.helper.Helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<TrackItem> items = new ArrayList<>();
    private OnItemListener mOnItemListener;
    private Context context;

    public ItemAdapter(Context context){
        this.context = context;
    }

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

        int num_of_days = Helper.getNumberofDays(new Date(), trackItem.getDateExpiry());
        holder.textViewDate.setText(date_n);
        String status;

        if(num_of_days == 2){
            status = "Expiring tomorrow";
        }
        else if(num_of_days == 1){
            status = "Expiring today";
        }
        else if(num_of_days <= 0){
            status = "Expired!";
        }
        else{
            status = num_of_days + " days remaining";
        }

        holder.textViewReaminingDays.setText(status);


        if(num_of_days > 7){
            holder.trackItemLogo.setColorFilter(context.getResources().getColor(R.color.green));
            holder.textViewDate.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.textViewReaminingDays.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if(num_of_days > 2){
            holder.trackItemLogo.setColorFilter(context.getResources().getColor(R.color.yellow));
            holder.textViewDate.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            holder.textViewReaminingDays.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else if (num_of_days <= 2){
            holder.trackItemLogo.setColorFilter(context.getResources().getColor(R.color.red));
            holder.textViewDate.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.textViewReaminingDays.setTextColor(context.getResources().getColor(R.color.red));
        }


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

    public void setOnItemListener(OnItemListener onItemListener){
        mOnItemListener = onItemListener;
    }

    public TrackItem getTrackItemAt(int position){
        return items.get(position);
    }



    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView trackItemLogo;
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
            trackItemLogo = itemView.findViewById(R.id.item_track);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(onItemListener != null && position != RecyclerView.NO_POSITION)
                onItemListener.onItemClick(position);
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }


}