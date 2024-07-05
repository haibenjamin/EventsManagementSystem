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
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.R;
import com.example.client.Util.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private ArrayList<EventData> events;

    public EventAdapter(ArrayList<EventData> events) {
        this.events = events;
    }

    private GuestCallBack guestCallback;
    private EventCallBack eventCallBack;

    public void setGuestCallback(GuestCallBack guestCallback) {
        this.guestCallback = guestCallback;
    }
    public void setEventCallback(EventCallBack eventCallBack) {
        this.eventCallBack = eventCallBack;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventViewHolder eventViewHolder = new EventViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
       EventData eventData = getItem(position);
        holder.event_name_txt.setText(eventData.getName());
        holder.event_location_txt.setText(eventData.getLocation() + "");
        holder.event_budget_txt.setText(eventData.getBudget() + "");
        holder.event_date_txt.setText(eventData.getDate() + "");
        holder.event_type_txt.setText(eventData.getType());
        setEventTypeImg(holder,position,eventData);
    }

    private void setEventTypeImg(EventViewHolder holder, int position, EventData eventData) {
        switch (eventData.getType()){
            case "Wedding":{holder.type_icon.setImageResource(R.drawable.couple);}
                break;
            case "Conference":{holder.type_icon.setImageResource(R.drawable.conference);}
            break;
            case "Birthday":{holder.type_icon.setImageResource(R.drawable.birthday);}
            break;
            case "Company Event":{holder.type_icon.setImageResource(R.drawable.company);}
            break;
            case "Bar/Bat Mitzva":{holder.type_icon.setImageResource(R.drawable.mitzva);}
            break;

        }
    }

    @Override
    public int getItemCount() {
        return this.events == null ? 0 : this.events.size();
    }

    private EventData getItem(int position) {
        return this.events.get(position);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView event_delete_btn,type_icon;
        private MaterialTextView event_name_txt,event_location_txt,event_budget_txt,event_date_txt,event_type_txt;
        private Button event_guests_btn,event_tasks_btn,event_vendors_btn;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            event_delete_btn = itemView.findViewById(R.id.event_delete_btn);
            event_tasks_btn=itemView.findViewById(R.id.event_tasks_btn);
            event_guests_btn=itemView.findViewById(R.id.event_guests_btn);
            event_vendors_btn=itemView.findViewById(R.id.event_vendors_btn);
            event_name_txt = itemView.findViewById(R.id.event_name_txt);
            event_date_txt = itemView.findViewById(R.id.event_date_txt);
            event_budget_txt = itemView.findViewById(R.id.event_budget_txt);
            event_location_txt = itemView.findViewById(R.id.event_location_txt);
            event_type_txt=itemView.findViewById(R.id.event_type_txt);
            type_icon=itemView.findViewById(R.id.event_type_icon);


            event_delete_btn.setOnClickListener(v -> {
                if(eventCallBack!=null)
                    eventCallBack.deleteClicked(getAdapterPosition());

            });
            event_guests_btn.setOnClickListener(v->{
                if(eventCallBack!=null)
                    eventCallBack.guestsClicked(getAdapterPosition());

            });

            event_tasks_btn.setOnClickListener(v->{
                if(eventCallBack!=null)
                    eventCallBack.tasksClicked(getAdapterPosition());
            });
            event_vendors_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(eventCallBack!=null)
                        eventCallBack.vendorsClicked(getAdapterPosition());
                }
            });


        }
    }
}
