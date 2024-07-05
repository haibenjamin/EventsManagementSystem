package com.example.client.Callback;

import androidx.cardview.widget.CardView;

import com.example.client.Model.SugVendor;


public interface NegVendorCallBack {

    void acceptClicked(SugVendor vendor, int position);
    void declineClicked(SugVendor vendor, int position);
}
