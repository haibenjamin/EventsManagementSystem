package com.example.client.Adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Model.Guest;
import com.example.client.Callback.GuestCallBack;
import com.example.client.R;
import com.example.client.Util.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class GuestAdapter extends RecyclerView.Adapter<GuestAdapter.GuestViewHolder> {

    private ArrayList<Guest> guests;

    public GuestAdapter(ArrayList<Guest> guests) {
        this.guests = guests;
    }

    private GuestCallBack guestCallback;

    public void setGuestCallback(GuestCallBack guestCallback) {
        this.guestCallback = guestCallback;
    }

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_item, parent, false);
        GuestViewHolder guestViewHolder = new GuestViewHolder(view);
        return guestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        Guest guest = getItem(position);
        holder.guest_name_txt.setText(guest.getName());
        holder.guest_phone_txt.setText(guest.getPhoneNumber() + "");
        holder.guest_status_txt.setText(guest.getStatus() + "");
        holder.guest_group_txt.setText(guest.getGroup());
        holder.guest_comments_txt.setText(guest.getComments() + "");
        holder.guest_guests_txt.setText(guest.getPeopleCount() + "");
     /*   if (guest.isComing())
            ImageLoader.getInstance().loadImage(R.drawable.coming_fill_icon, holder.guest_coming_icon);
        else
            ImageLoader.getInstance().loadImage(R.drawable.coming_empty_icon, holder.guest_coming_icon);
*/

    }

    @Override
    public int getItemCount() {
        return this.guests == null ? 0 : this.guests.size();
    }

    private Guest getItem(int position) {
        return this.guests.get(position);
    }

    public class GuestViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView guest_coming_icon;
        private ShapeableImageView guest_icon;
        private MaterialTextView guest_name_txt;
        private MaterialTextView guest_phone_txt;
        private MaterialTextView guest_status_txt;
        private MaterialTextView guest_comments_txt;
        private MaterialTextView guest_guests_txt;
        private MaterialTextView guest_group_txt;


        public GuestViewHolder(@NonNull View itemView) {
            super(itemView);
            guest_coming_icon = itemView.findViewById(R.id.guest_coming_icon);
            guest_name_txt = itemView.findViewById(R.id.guest_name_txt);
            guest_status_txt = itemView.findViewById(R.id.guest_status_txt);
            guest_phone_txt = itemView.findViewById(R.id.guest_phone_txt);
            guest_comments_txt = itemView.findViewById(R.id.guest_comments_txt);
            guest_group_txt = itemView.findViewById(R.id.guest_group_txt);
            guest_guests_txt = itemView.findViewById(R.id.guest_guests_txt);
            guest_icon = itemView.findViewById(R.id.guest_icon);
            guest_status_txt.setPaintFlags(guest_status_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            guest_guests_txt.setPaintFlags(guest_guests_txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            guest_coming_icon.setOnClickListener(v -> {
                if (guestCallback != null)
                    guestCallback.comingClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
            guest_status_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (guestCallback != null)
                        guestCallback.statusClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
            guest_guests_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (guestCallback != null)
                        guestCallback.guestsClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
            guest_comments_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (guestCallback != null)
                        guestCallback.commentClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }
}
