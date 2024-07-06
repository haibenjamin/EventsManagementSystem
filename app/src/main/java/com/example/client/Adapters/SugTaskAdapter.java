package com.example.client.Adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Callback.GuestCallBack;
import com.example.client.Callback.TaskCallBack;
import com.example.client.Model.Guest;
import com.example.client.Model.Task;
import com.example.client.Model.sugTask;
import com.example.client.R;
import com.example.client.Util.DataManager;
import com.example.client.Util.ImageLoader;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Map;

public class SugTaskAdapter extends RecyclerView.Adapter<SugTaskAdapter.TaskViewHolder> {


    private ArrayList<String> tasks;
    private TaskCallBack taskCallBack;

    public SugTaskAdapter(ArrayList<String> tasks) {
        this.tasks = tasks;
    }



    public void setTaskCallBack(TaskCallBack taskCallBack) {
        this.taskCallBack = taskCallBack;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sug_task_item, parent, false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        String task = getItem(position);
        holder.task_task_txt.setText(task);
    }
    @Override
    public int getItemCount() {
        return this.tasks == null ? 0 : this.tasks.size();
    }

    private String getItem(int position) {
        return this.tasks.get(position);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView task_task_txt;
        private CardView task_task_cv;


        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            task_task_txt = itemView.findViewById(R.id.sug_task_txt);
            task_task_cv=itemView.findViewById(R.id.task_card_view);
            task_task_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(taskCallBack!=null)
                        taskCallBack.taskClicked(task_task_cv,task_task_txt.getText().toString(),getAdapterPosition());
                }
            });
        }
    }
}
