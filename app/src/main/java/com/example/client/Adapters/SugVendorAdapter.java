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

import com.example.client.Callback.SugVendorCallBack;
import com.example.client.Callback.VendorCallBack;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Vendor;
import com.example.client.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

    public class SugVendorAdapter extends RecyclerView.Adapter<SugVendorAdapter.VendorViewHolder>{

        private ArrayList<SugVendor> vendors;
        private SugVendorCallBack vendorCallBack;
        public static final int ADDED_VENDORS = 0;
        public static final int SUGGESTED_VENDORS = 1;
        public static final int NEGOTIATED_VENDORS = 2;
        private int viewType;

        public SugVendorAdapter(ArrayList<SugVendor> vendors, int viewType) {
            this.vendors = vendors;
            this.viewType = viewType;
        }


        public void setVendorCallBack(SugVendorCallBack vendorCallBack) {
            this.vendorCallBack = vendorCallBack;
        }

        @NonNull
        @Override
        public SugVendorAdapter.VendorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_item, parent, false);
            SugVendorAdapter.VendorViewHolder vendorViewHolder = new SugVendorAdapter.VendorViewHolder(view);
            return vendorViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull SugVendorAdapter.VendorViewHolder holder, int position) {
            setViews(holder,position);
        }

        private void setViews(VendorViewHolder holder, int position) {
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
            return this.vendors == null ? 0 : this.vendors.size();
        }

        private SugVendor getItem(int position) {
            return this.vendors.get(position);
        }

        public class VendorViewHolder extends RecyclerView.ViewHolder {
            private CardView vendor_vendor_cv;
            private ShapeableImageView delBtn, acceptBtn, declineBtn, logo;
            private TextView vendor_name_txt, vendor_mail_txt, vendor_type_txt, vendor_leads_txt;
            private LinearLayout layout_linear_price;
            private Button contactBtn;


            public VendorViewHolder(@NonNull View itemView) {
                super(itemView);
                vendor_name_txt = itemView.findViewById(R.id.vendor_name_txt);
                vendor_mail_txt = itemView.findViewById(R.id.vendor_mail_txt);
                vendor_leads_txt = itemView.findViewById(R.id.vendor_leads_txt);
                vendor_type_txt = itemView.findViewById(R.id.vendor_type_txt);
                vendor_vendor_cv=itemView.findViewById(R.id.task_card_view);
                contactBtn=itemView.findViewById(R.id.vendor_contact_btn);
                acceptBtn=itemView.findViewById(R.id.vendor_accept_btn);
                declineBtn=itemView.findViewById(R.id.vendor_decline_btn);
                delBtn=itemView.findViewById(R.id.vendor_delete_btn);
                layout_linear_price=itemView.findViewById(R.id.layout_linear_price);

                contactBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(vendorCallBack!=null)
                            vendorCallBack.contactClicked(getItem(getAdapterPosition()),getAdapterPosition());
                    }
                });
            }
        }
    }

