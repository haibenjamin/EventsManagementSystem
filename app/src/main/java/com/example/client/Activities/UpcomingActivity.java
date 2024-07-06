package com.example.client.Activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.client.Adapters.UpcomingAdapter;
import com.example.client.Callback.UpcomingCallBack;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Model.Upcoming;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navView;
    ShapeableImageView deleteBtn;
    UpcomingAdapter upcomingAdapter;
    RecyclerView upcomingList;
    TextView upcoming_title_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        getUpcomingEventsFromServer();
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);
        initViews();
    }

    private void initViews() {
        upcomingAdapter = new UpcomingAdapter(DataManager.getUpcomingEvents());
        updateRecyclerView(upcomingAdapter);
    }

    private void updateRecyclerView(UpcomingAdapter upcomingAdapter) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        upcomingList.setAdapter(upcomingAdapter);
        upcomingList.setLayoutManager(linearLayoutManager);
        upcomingAdapter.setUpcomingCallback(new UpcomingCallBack() {

            @Override
            public void deleteClicked(Upcoming upcoming, int position) {
                String userId= ConnectionManager.getUserId();
                String eventId=DataManager.getUpcomingEventId(position);
                deleteEvents(userId,eventId);
                DataManager.deleteUpcomingEvent(position);
                upcomingList.getAdapter().notifyDataSetChanged();

            }
    });
    }

    private void deleteEvents(String userId, String eventId) {
        Call<MyResponse> call = ConnectionManager.apiService.deleteUpcomingEvent(userId,eventId);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()){}
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void findViews() {
     navView=findViewById(R.id.nav_view);
     deleteBtn=findViewById(R.id.upcoming_delete_btn);
    upcomingList=findViewById(R.id.upcoming_list_rv);
    upcoming_title_txt=findViewById(R.id.no_upcoming_events_tv);
    upcoming_title_txt.setVisibility(View.VISIBLE);
    }

    private void getUpcomingEventsFromServer() {
        ConnectionManager.initRetrofit();
        Call<ArrayList<Upcoming>> call= ConnectionManager.apiService.getUpcomingEvents(ConnectionManager.getUserId());
        call.enqueue(new Callback<ArrayList<Upcoming>>() {
            @Override
            public void onResponse(Call<ArrayList<Upcoming>> call, Response<ArrayList<Upcoming>> response) {
                if (response.isSuccessful())
                {
                    ArrayList<Upcoming> events = response.body();
                    DataManager.setUpcomingList(events);
                    if (events.size()>0)
                        upcoming_title_txt.setVisibility(View.GONE);
                    initViews();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Upcoming>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }
}