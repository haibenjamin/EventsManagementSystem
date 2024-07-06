package com.example.client.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.client.Model.Responses.MyResponse;
import com.example.client.R;
import com.example.client.Util.Connection.AuthManager;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.Connection.RetrofitClient;
import com.example.client.Util.MenuManager;
import com.example.client.Util.MySharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button loginBtn, registerBtn, logoutBtn,eventsBtn,upcomingEventsBtn;
    boolean loginBtnVis = true, logoutBtnVis = false, registBtnVis = true,eventsBtnVis=false,upcomingEventsVis=false;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpBackGround();
        setupDrawer();
        findViews();
        isConnected();
        initViews();
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSharedPref(false);
                isConnected();
                logout();
                initViews();

            }
        });
        eventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,EventsActivity.class));
            }
        });
        upcomingEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UpcomingActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    private void setUpBackGround() {
        RelativeLayout relativeLayout = findViewById(R.id.root_layout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(2000);
    }

    public void isConnected() {
        boolean isLoggedIn = MySharedPrefManager.getInstance().getSharedPref(MainActivity.this);
        if (isLoggedIn && ConnectionManager.getUserId() != null) {
            eventsBtnVis=true;
            logoutBtnVis = true;
            registBtnVis = false;
            loginBtnVis = false;
            if (AuthManager.getInstance().getRole().equals("Vendor"))
                upcomingEventsVis=true;
        } else {
            eventsBtnVis=false;
            logoutBtnVis = false;
            loginBtnVis = true;
            registBtnVis = true;
            upcomingEventsVis=false;
        }

    }

    private void initViews() {
        if (loginBtnVis)
            loginBtn.setVisibility(View.VISIBLE);
        else
            loginBtn.setVisibility(View.INVISIBLE);
        if (logoutBtnVis)
            logoutBtn.setVisibility(View.VISIBLE);
        else
            logoutBtn.setVisibility(View.INVISIBLE);
        if (registBtnVis)
            registerBtn.setVisibility(View.VISIBLE);
        else
            registerBtn.setVisibility(View.INVISIBLE);
        if(eventsBtnVis)
            eventsBtn.setVisibility(View.VISIBLE);
            else
                eventsBtn.setVisibility(View.GONE);
        if(upcomingEventsVis)
            upcomingEventsBtn.setVisibility(View.VISIBLE);
        else
            upcomingEventsBtn.setVisibility(View.GONE);


    }

    private void findViews() {

        navigationView = findViewById(R.id.nav_view);
        loginBtn = findViewById(R.id.main_login_btn);
        registerBtn = findViewById(R.id.main_register_btn);
        logoutBtn = findViewById(R.id.main_logout_btn);
        eventsBtn=findViewById(R.id.main_my_events_btn);
        upcomingEventsBtn=findViewById(R.id.main_upcoming_btn);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return super.onNavigationItemSelected(item);
    }

    private void logout() {
        RetrofitClient.clearCookies();
        Call<MyResponse> call = ConnectionManager.apiService.getLogout();
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.i("RESPONSE_LOGOUT", response.code() + " " + response.message());
                AuthManager.clearJWT(MainActivity.this);
                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private void setSharedPref(boolean state) {
        SharedPreferences sharedPreferences = getSharedPreferences("EventPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", state);
        editor.apply();
    }

    private void hideItem() {
        MenuManager.getInstance().getNavMenu().findItem(R.id.nav_logout).setVisible(true);

    }

}