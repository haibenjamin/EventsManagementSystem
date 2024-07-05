package com.example.client.Callback;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.client.Model.Guest;
import com.example.client.Model.Task;

public interface TaskCallBack {
    void taskClicked(CardView taskView,Task task, int position);
    void taskClicked(CardView taskView,String task, int position);
    void deleteClicked(Task task,int position);
}
