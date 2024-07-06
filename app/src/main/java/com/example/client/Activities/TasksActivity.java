package com.example.client.Activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.Adapters.GuestAdapter;
import com.example.client.Adapters.SugTaskAdapter;
import com.example.client.Adapters.TaskAdapter;
import com.example.client.Callback.GuestCallBack;
import com.example.client.Callback.TaskCallBack;
import com.example.client.Model.Guest;
import com.example.client.Model.NewTask;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.Model.Responses.GuestResponse;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Model.Responses.TaskResponse;
import com.example.client.Model.Task;
import com.example.client.Model.TasksArray;
import com.example.client.Model.sugTask;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageButton backlogDownBtn, todoUpBtn, todoDownBtn, inprogUpBtn, inprogDownBtn, completeUpBtn, backlogAddBtn, todoAddBtn, inprogAddBtn, completeAddBtn;
    Button saveBtn, sugBtn;
    RecyclerView backlogList, todoList, inprogList, completeList;
    TaskAdapter taskAdapter;
    ShapeableImageView delBtn;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        getTasksFromServer();
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);
        initViews();
        sugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuggestedTasks();
            }
        });
        backlogDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("Backlog", "TODO");
            }
        });
        todoDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("TODO", "In progress");
            }
        });
        todoUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("TODO", "Backlog");
            }
        });

        inprogDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("In progress", "Complete");


            }
        });
        inprogUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("In progress", "TODO");
            }
        });
        completeUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTasks("Complete", "In progress");
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tasks_backlog_add_btn: {
                        addTask("Backlog");
                    }
                    break;
                    case R.id.tasks_todo_add_btn: {
                        addTask("TODO");
                    }
                    break;
                    case R.id.tasks_inprog_add_btn: {
                        addTask("In progress");
                    }
                    break;
                    case R.id.tasks_complete_add_btn: {
                        addTask("Complete");
                    }
                    break;

                }

            }
        };
        todoAddBtn.setOnClickListener(listener);
        completeAddBtn.setOnClickListener(listener);
        backlogAddBtn.setOnClickListener(listener);
        inprogAddBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTasks();
            }
        });


    }


    private void showSuggestedTasks() {
        SugTaskAdapter sugTasksAdapter = new SugTaskAdapter(DataManager.getSuggestedTasksToString());

// Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.sugg_tasks_dialog, null);

// Find RecyclerView inside the inflated dialog layout
        RecyclerView suggList = dialogLayout.findViewById(R.id.dialog_suggested_rv);
        updateRecyclerView(sugTasksAdapter, suggList);

// Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the OK button click
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void getTasksFromServer() {
        String userId = ConnectionManager.getUserId();
        Call<TaskResponse> call = ConnectionManager.apiService.getTasks(userId, ConnectionManager.getSelectedEventId());
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    TaskResponse taskResponse = response.body();
                    if (taskResponse != null) {
                        ArrayList<Task> tasks = taskResponse.getTasks();
                        ArrayList<sugTask> suggestedTasks = taskResponse.getSuggestedTasks();
                        DataManager.setTasks(tasks, suggestedTasks);
                        initViews();

                    }
                }

            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.i("ERROR", t.toString());
            }
        });
    }

    private void saveTasks() {
        TasksArray tasks = new TasksArray();
        ArrayList<Task> updatedTasksLocal = DataManager.getTasks();
        tasks.setTasks(updatedTasksLocal);
        Call call = ConnectionManager.apiService.updateTasks(ConnectionManager.getUserId(), ConnectionManager.getSelectedEventId(), tasks);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TasksActivity.this,"Task Updated Successfully",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void addTask(String column) {
        showAlertDialog(column);
    }

    private void showAlertDialog(String column) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_task_cutom_dialog, null);
        final EditText editText = dialogLayout.findViewById(R.id.task_dialog_content_et);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("New Task");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the OK button click
                String inputText = editText.getText().toString();
                if (inputText != null) {
                    Task task = new Task(inputText, column);

                    DataManager.addTask(task);
                    postAddTask(task);
                    initViews();
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private JsonObject convertTaskToJson(Task task) {
        JsonObject newCard = new JsonObject();
        newCard.addProperty("title", task.getTitle());
        newCard.addProperty("column", task.getColumn());

        JsonObject data = new JsonObject();
        data.add("newCard", newCard);

        return data;
    }


    private void postAddTask(Task task) {
        JsonObject newCard = null;
        try {
            newCard = convertTaskToJson(task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Call<TaskResponse> call = ConnectionManager.apiService.addTask(
                ConnectionManager.getUserId(),
                ConnectionManager.getSelectedEventId(),
                newCard
        );

        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TasksActivity.this,"Task Added Successfully",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TasksActivity.this,"Error while trying to add the tasks",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void moveTasks(String from, String to) {
        ArrayList<Task> selected = getSelectedTasks(DataManager.getTaskByColumn(from));

        // Update the column of the selected tasks locally
        for (Task task : selected) {
            task.setSelected();
            task.setColumn(to);
        }
        // Verify DataManager has updated tasks
        ArrayList<Task> updatedTasksLocal = DataManager.getTasks();
        saveTasks();
        initViews(); // Assuming this refreshes the UI with the updated local tasks


    }

    private void fetchUpdatedTasksWithDelay(String userId, String eventId) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> fetchUpdatedTasks(userId, eventId), 1000);  // 1-second delay
    }

    private void fetchUpdatedTasks(String userId, String eventId) {
        Call<TaskResponse> fetchCall = ConnectionManager.apiService.getTasks(userId, eventId);
        fetchCall.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Task> updatedTasks = response.body().getTasks();
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                Log.e("FETCH FAILURE", "Failed to fetch updated tasks. Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }


    private void updateTaskList() {
        String userId = ConnectionManager.getUserId();
        String eventID = ConnectionManager.getSelectedEventId();
        Call<TaskResponse> call = ConnectionManager.apiService.getTasks(userId, eventID);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful()) {
                    TaskResponse taskResponse = response.body();
                    if (taskResponse != null) {
                        ArrayList<Task> tasks = taskResponse.getTasks();
                        ArrayList<sugTask> sugTasks = taskResponse.getSuggestedTasks();
                        DataManager.setTasks(tasks, sugTasks);
                    }
                }

            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private ArrayList<Task> getSelectedTasks(ArrayList<Task> tasks) {
        ArrayList<Task> selected = new ArrayList<>();
        for (Task task : tasks
        ) {
            if (task.getSelected())
                selected.add(task);

        }
        return selected;
    }

    private int getCardBackgroundColor(CardView cardView) {
        Drawable background = cardView.getBackground();
        if (background.getClass().getName().equals("androidx.cardview.widget.RoundRectDrawable")) {
            {
                try {
                    Field field = background.getClass().getDeclaredField("mColor");
                    field.setAccessible(true);
                    return (int) field.get(background);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();

                }
            }
        }
        return Color.TRANSPARENT;
    }

    private void updateRecyclerView(SugTaskAdapter sugTaskAdapter, RecyclerView sugList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        sugList.setAdapter(sugTaskAdapter);
        sugList.setLayoutManager(linearLayoutManager);
        sugTaskAdapter.setTaskCallBack(new TaskCallBack() {
            @Override
            public void taskClicked(CardView taskView, Task task, int position) {

            }

            @Override
            public void taskClicked(CardView taskView, String task, int position) {
                    taskView.setVisibility(View.GONE);
                    DataManager.deleteSuggTask(task);
                    SugTaskAdapter sugTasksAdapter = new SugTaskAdapter(DataManager.getSuggestedTasksToString());
                    updateRecyclerView(sugTasksAdapter, sugList);
                    postAddTask(new Task(task, "Backlog"));
                    initViews();


            }

            @Override
            public void deleteClicked(Task task, int position) {

            }
        });
    }

    private boolean checkNotHeader(String task) {
        String[] headers = {"decorations", "music & entertainment", "food & drinks", "vendor", "setup", "logistics"};
        for (String str : headers)
            if (task.equals(str))
                return false;
        return true;
    }

    private void updateRecyclerView(TaskAdapter todoTaskAdapter,
                                    TaskAdapter inProgressTaskAdapter,
                                    TaskAdapter backlogTaskAdapter,
                                    TaskAdapter completeTaskAdapter) {

        LinearLayoutManager todoLayoutManager = new LinearLayoutManager(this);
        todoLayoutManager.setOrientation(RecyclerView.VERTICAL);
        todoList.setAdapter(todoTaskAdapter);
        todoList.setLayoutManager(todoLayoutManager);
        todoTaskAdapter.setTaskCallBack(new TaskCallBack() {
            @Override
            public void taskClicked(CardView taskView, Task task, int position) {
                selectTask(taskView, task, position);


            }

            @Override
            public void taskClicked(CardView taskView, String task, int position) {

            }

            @Override
            public void deleteClicked(Task task, int position) {
                DataManager.getTasks().remove(task);
                deleteTask(task);
                todoList.getAdapter().notifyDataSetChanged();
                initViews();
            }
        });

        LinearLayoutManager inprogLayoutManager = new LinearLayoutManager(this);
        inprogLayoutManager.setOrientation(RecyclerView.VERTICAL);
        inprogList.setAdapter(inProgressTaskAdapter);
        inprogList.setLayoutManager(inprogLayoutManager);
        inProgressTaskAdapter.setTaskCallBack(new TaskCallBack() {
            @Override
            public void taskClicked(CardView taskView, Task task, int position) {
                selectTask(taskView, task, position);
            }

            @Override
            public void taskClicked(CardView taskView, String task, int position) {

            }

            @Override
            public void deleteClicked(Task task, int position) {
                DataManager.getTasks().remove(task);
                deleteTask(task);
                inprogList.getAdapter().notifyDataSetChanged();
                initViews();
            }
        });

        LinearLayoutManager backlogLayoutManager = new LinearLayoutManager(this);
        backlogLayoutManager.setOrientation(RecyclerView.VERTICAL);
        backlogList.setAdapter(backlogTaskAdapter);
        backlogList.setLayoutManager(backlogLayoutManager);
        backlogTaskAdapter.setTaskCallBack(new TaskCallBack() {
            @Override
            public void taskClicked(CardView taskView, Task task, int position) {
                selectTask(taskView, task, position);
            }

            @Override
            public void taskClicked(CardView taskView, String task, int position) {

            }

            @Override
            public void deleteClicked(Task task, int position) {
                DataManager.getTasks().remove(task);
                deleteTask(task);
                backlogList.getAdapter().notifyDataSetChanged();
                initViews();
            }
        });

        LinearLayoutManager completeLayoutManager = new LinearLayoutManager(this);
        completeLayoutManager.setOrientation(RecyclerView.VERTICAL);
        completeList.setAdapter(completeTaskAdapter);
        completeList.setLayoutManager(completeLayoutManager);
        completeTaskAdapter.setTaskCallBack(new TaskCallBack() {
            @Override
            public void taskClicked(CardView taskView, Task task, int position) {
                selectTask(taskView, task, position);
            }

            @Override
            public void taskClicked(CardView taskView, String task, int position) {

            }

            @Override
            public void deleteClicked(Task task, int position) {
                Log.i("CLICKED DELETE TASK COMPELTE", position + "");
                DataManager.getTasks().remove(task);
                deleteTask(task);
                completeList.getAdapter().notifyDataSetChanged();
                initViews();
            }
        });
    }

    private void deleteTask(Task task) {
        String userId, eventId, taskId;
        userId = ConnectionManager.getUserId();
        eventId = ConnectionManager.getSelectedEventId();
        taskId = task.getId();
        Call call = ConnectionManager.apiService.deleteTask(userId, eventId, taskId);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TasksActivity.this,"Task Deleted Successfully",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void selectTask(CardView taskView, Task task, int position) {
        task.setSelected();

        if (task.getSelected()) {
            taskView.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
        } else {
            taskView.setCardBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    private void initViews() {
        TaskAdapter todoTaskAdapter = new TaskAdapter(DataManager.getTaskByColumn("TODO"));
        TaskAdapter inProgressTaskAdapter = new TaskAdapter(DataManager.getTaskByColumn("In progress"));
        TaskAdapter backlogTaskAdapter = new TaskAdapter(DataManager.getTaskByColumn("Backlog"));
        TaskAdapter completeTaskAdapter = new TaskAdapter(DataManager.getTaskByColumn("Complete"));
        updateRecyclerView(todoTaskAdapter,
                inProgressTaskAdapter,
                backlogTaskAdapter,
                completeTaskAdapter);


    }

    private void findViews() {
        backlogDownBtn = findViewById(R.id.tasks_backlog_down_btn);
        todoUpBtn = findViewById(R.id.tasks_todo_up_btn);
        todoDownBtn = findViewById(R.id.tasks_todo_down_btn);
        inprogUpBtn = findViewById(R.id.tasks_inprog_up_btn);
        inprogDownBtn = findViewById(R.id.tasks_inprog_down_btn);
        completeUpBtn = findViewById(R.id.tasks_complete_up_btn);
        backlogList = findViewById(R.id.tasks_backlog_rv);
        todoList = findViewById(R.id.tasks_todo_rv);
        inprogList = findViewById(R.id.tasks_inprog_rv);
        completeList = findViewById(R.id.tasks_complete_rv);
        navView = findViewById(R.id.nav_view);
        delBtn = findViewById(R.id.task_delete_img);
        backlogAddBtn = findViewById(R.id.tasks_backlog_add_btn);
        todoAddBtn = findViewById(R.id.tasks_todo_add_btn);
        completeAddBtn = findViewById(R.id.tasks_complete_add_btn);
        inprogAddBtn = findViewById(R.id.tasks_inprog_add_btn);
        saveBtn = findViewById(R.id.tasks_save_btn);
        sugBtn = findViewById(R.id.tasks_suggested_btn);
    }
}