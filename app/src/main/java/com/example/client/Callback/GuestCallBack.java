package com.example.client.Callback;

import android.widget.TextView;

import com.example.client.Model.Guest;

public interface GuestCallBack {
    void deleteClicked(Guest guest, int position);
    void statusClicked(Guest guest, int position);
    void guestsClicked(Guest guest, int position);
    void commentClicked(Guest guest, int position);
}
