package com.example.client.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Callback.EventCallBack;
import com.example.client.Callback.GuestCallBack;
import com.example.client.Callback.UpcomingCallBack;
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.Upcoming;
import com.example.client.R;
import com.example.client.Util.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    private ArrayList<Upcoming> events;
    private UpcomingCallBack callBack;

    public UpcomingAdapter(ArrayList<Upcoming> events) {
        this.events = events;
    }

    public void setUpcomingCallback(UpcomingCallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_item, parent, false);
        UpcomingViewHolder eventViewHolder = new UpcomingViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        Upcoming eventData = getItem(position);
        holder.event_name_txt.setText(eventData.getName());
        holder.event_location_txt.setText(eventData.getLocation() + "");
        holder.event_mail_txt.setText(eventData.getOwner().getEmail() + "");
        holder.event_date_txt.setText(eventData.getDate());
        holder.event_type_txt.setText(eventData.getType());
    }


    @Override
    public int getItemCount() {
        return this.events == null ? 0 : this.events.size();
    }

    private Upcoming getItem(int position) {
        return this.events.get(position);
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView event_delete_btn,type_icon;
        private MaterialTextView event_name_txt,event_location_txt,event_date_txt,event_type_txt,event_mail_txt;


        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            event_delete_btn = itemView.findViewById(R.id.upcoming_delete_btn);
            event_name_txt = itemView.findViewById(R.id.upcoming_name_txt);
            event_date_txt = itemView.findViewById(R.id.upcoming_date_txt);
            event_location_txt = itemView.findViewById(R.id.upcoming_location_txt);
            event_type_txt=itemView.findViewById(R.id.upcoming_type_txt);
            event_mail_txt=itemView.findViewById(R.id.upcoming_mail_txt);
            type_icon=itemView.findViewById(R.id.event_type_icon);


            event_delete_btn.setOnClickListener(v -> {
                if(callBack!=null)
                    callBack.deleteClicked(getItem(getAdapterPosition()),getAdapterPosition());
            });
        }
    }
}
