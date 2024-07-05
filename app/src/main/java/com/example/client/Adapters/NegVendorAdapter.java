package com.example.client.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Callback.GuestCallBack;
import com.example.client.Callback.NegVendorCallBack;
import com.example.client.Callback.TaskCallBack;
import com.example.client.Model.Guest;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Task;
import com.example.client.R;
import com.example.client.Util.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NegVendorAdapter extends RecyclerView.Adapter<NegVendorAdapter.NegVendorViewHolder> {

    private ArrayList<SugVendor> negVendors;
    private NegVendorCallBack negVendorCallBack;
    public static final int ADDED_VENDORS = 0;
    public static final int SUGGESTED_VENDORS = 1;
    public static final int NEGOTIATED_VENDORS = 2;
    private int viewType;

    public NegVendorAdapter(ArrayList<SugVendor> negVendors,int viewType) {
        this.negVendors = negVendors;
        this.viewType = viewType;
    }



    public void setNegVendorCallBack(NegVendorCallBack negVendorCallBack) {
        this.negVendorCallBack = negVendorCallBack;
    }

    @NonNull
    @Override
    public NegVendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_item, parent, false);
        NegVendorViewHolder negVendoriewHolder = new NegVendorViewHolder(view);
        return negVendoriewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NegVendorViewHolder holder, int position) {
      //  Task task = getItem(position);
       // holder.task_task_txt.setText(task.getTitle());
        setViews(holder,position);
    }

    private void setViews(@NonNull NegVendorViewHolder holder, int position) {
        SugVendor vendor = getItem(position);
        holder.vendor_name_txt.setText(vendor.getBusinessName());
        holder.vendor_mail_txt.setText(vendor.getEmail());
        holder.vendor_type_txt.setText(vendor.getBusinessType());
        holder.vendor_leads_txt.setText("" + vendor.getLeadCount());

        switch (viewType) {
            case ADDED_VENDORS: {
                holder.contactBtn.setVisibility(View.GONE);
                holder.declineBtn.setVisibility(View.GONE);
                holder.acceptBtn.setVisibility(View.GONE);
                holder.delBtn.setVisibility(View.VISIBLE);
            }
            break;
            case NEGOTIATED_VENDORS: {
                holder.contactBtn.setVisibility(View.GONE);
                holder.declineBtn.setVisibility(View.VISIBLE);
                holder.acceptBtn.setVisibility(View.VISIBLE);
                holder.delBtn.setVisibility(View.GONE);
                holder.layout_linear_price.setVisibility(View.GONE);
            }
            break;
            case SUGGESTED_VENDORS: {
                holder.contactBtn.setVisibility(View.VISIBLE);
                holder.declineBtn.setVisibility(View.GONE);
                holder.acceptBtn.setVisibility(View.GONE);
                holder.delBtn.setVisibility(View.GONE);
                holder.layout_linear_price.setVisibility(View.GONE);
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        return this.negVendors == null ? 0 : this.negVendors.size();
    }

    private SugVendor getItem(int position) {
        return this.negVendors.get(position);
    }

    public class NegVendorViewHolder extends RecyclerView.ViewHolder {
        private CardView vendor_vendor_cv;
        private ShapeableImageView delBtn, acceptBtn, declineBtn, logo;
        private TextView vendor_name_txt, vendor_mail_txt, vendor_type_txt, vendor_leads_txt;
        private LinearLayout layout_linear_price;
        private Button contactBtn;


        public NegVendorViewHolder(@NonNull View itemView) {
            super(itemView);
            vendor_leads_txt = itemView.findViewById(R.id.vendor_leads_txt);
            vendor_name_txt = itemView.findViewById(R.id.vendor_name_txt);
            vendor_mail_txt = itemView.findViewById(R.id.vendor_mail_txt);
            vendor_type_txt = itemView.findViewById(R.id.vendor_type_txt);
            delBtn = itemView.findViewById(R.id.vendor_delete_btn);
            acceptBtn = itemView.findViewById(R.id.vendor_accept_btn);
            declineBtn = itemView.findViewById(R.id.vendor_decline_btn);
            contactBtn = itemView.findViewById(R.id.vendor_contact_btn);
            layout_linear_price = itemView.findViewById(R.id.layout_linear_price);
            logo = itemView.findViewById(R.id.vendor_type_icon);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(negVendorCallBack!=null)
                        negVendorCallBack.acceptClicked(getItem(getAdapterPosition()),getAdapterPosition());
                }
            });
            declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(negVendorCallBack!=null)
                        negVendorCallBack.declineClicked(getItem(getAdapterPosition()),getAdapterPosition());
                }
            });



        }
    }
}
