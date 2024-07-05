package com.example.client.Activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.client.Adapters.EventAdapter;
import com.example.client.Callback.EventCallBack;
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.Model.Responses.GuestResponse;
import com.example.client.Model.Responses.RefreshResponse;
import com.example.client.Model.Responses.TaskResponse;
import com.example.client.Model.Task;
import com.example.client.R;
import com.example.client.Util.Connection.AuthManager;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.example.client.Util.MySharedPrefManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button createBtn,guestsBtn,tasksBtn,vendorsBtn;
    ShapeableImageView deleteBtn;
    EventAdapter eventAdapter;
    RecyclerView eventList;
    private NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);
        boolean isLoggedIn= MySharedPrefManager.getInstance().getSharedPref(EventsActivity.this);
        if (isLoggedIn){
            ConnectionManager.initRetrofit();
            String userId = ConnectionManager.getUserId(); // Your function to decode JWT and extract userId
            Log.i("USERID",userId);
            getEvents(userId);
            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(EventsActivity.this, CreateEventActivity.class));

                }
            });
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    DataManager.deleteEvent(position);
                }
            };

        }
        else
            startActivity(new Intent(EventsActivity.this,LoginActivity.class));

    }

    private void initViews() {
        //DataManager.initList();
        DataManager.convertEpochToFormmatedDate();
        eventAdapter = new EventAdapter(DataManager.getEvents());
        updateRecyclerView(eventAdapter);
    }

    private void updateRecyclerView(EventAdapter eventAdapter) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        eventList.setAdapter(eventAdapter);
        eventList.setLayoutManager(linearLayoutManager);
        eventAdapter.setEventCallback(new EventCallBack() {

            @Override
            public void deleteClicked(int position) {
                String userId=ConnectionManager.getUserId();
                String eventId=DataManager.getEventId(position);
                deleteEvents(userId,eventId);
                DataManager.deleteEvent(position);
                eventList.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void guestsClicked(int position) {
                String eventID = DataManager.getEventId(position);
                ConnectionManager.setSelectedEventId(eventID);
                startActivity(new Intent(EventsActivity.this, GuestsActivity.class));

            }

            @Override
            public void tasksClicked(int position) {
                String eventID = DataManager.getEventId(position);
                ConnectionManager.setSelectedEventId(eventID);
                startActivity(new Intent(EventsActivity.this, TasksActivity.class));
            }

            @Override
            public void vendorsClicked(int position) {
                String eventID = DataManager.getEventId(position);
                ConnectionManager.setSelectedEventId(eventID);
                startActivity(new Intent(EventsActivity.this, VendorsActivity.class));

            }
        });
    }
    private void deleteEvents(String userId,String eventId){
        Call<EventResponse> call = ConnectionManager.apiService.deleteEvent(userId,eventId);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.isSuccessful()){
                    Log.i("DELETED", eventId);
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }
    private void getRefreshToken(){
        ConnectionManager.initRetrofit();
        Call<RefreshResponse> call = ConnectionManager.apiService.getRefreshToken();
        call.enqueue(new Callback<RefreshResponse>() {
            @Override
            public void onResponse(Call<RefreshResponse> call, Response<RefreshResponse> response) {
                if (response.isSuccessful()){
                    Log.i("RESPONSE REFRESH","");
                    ConnectionManager.logout(EventsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<RefreshResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
    private void getEvents(String userId) {
        Call<EventResponse> call = ConnectionManager.apiService.getEvents(userId);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    EventResponse eventResponse = response.body();
                    if (eventResponse != null) {
                        ArrayList<EventData> events = eventResponse.getEvents();
                        DataManager.setEvents(events);
                        initViews();
                        Log.i("SUCCESS", "Retrieved " + events.size() + " events.");
                        for (EventData event : events) {
                            Log.i("EVENT", "Name: " + event.getName() + ", Date: " + event.getDate());
                            Log.i("EVENT_ID",event.getId());
                        }

                    } else {
                        Log.e("ERROR", "EventResponse is null");
                    }
                } else {
                    try {
                        if(response.code()!= HttpURLConnection.HTTP_UNAUTHORIZED) {
                            String msg = ConnectionManager.identifyMessage(response.errorBody().string());
                            Toast.makeText(EventsActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                        else{
                            Log.i("UNAUTHORIZED","REFRESHING");
                            Log.i("ACCESS TOKEN BEFORE REFRESH ",AuthManager.getInstance().getAccessToken());
                            getRefreshToken();

                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);

                    }
                    catch(Exception e){
                        if(response.code()== HttpURLConnection.HTTP_UNAUTHORIZED) {
                            //check for refresh token
                            getRefreshToken();

                            throw new RuntimeException(e);

                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                // Handle network error
                t.printStackTrace();
            }
        });
    }

    private void findViews() {

        eventList = findViewById(R.id.event_list_rv);
        createBtn = findViewById(R.id.event_create_btn);
        //deleteBtn=findViewById(R.id.event_delete_btn);
        navView=findViewById(R.id.nav_view);
        //vendorsBtn=findViewById(R.id.event_vendors_btn);



    }
}
