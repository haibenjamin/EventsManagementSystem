package com.example.client.Callback;

import com.example.client.Model.EventData;


public interface EventCallBack {
    void deleteClicked(int position);
    void guestsClicked(int position);
    void tasksClicked(int position);
    void vendorsClicked(int position);
}
