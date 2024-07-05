package com.example.client.Util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.client.Activities.CreateEventActivity;
import com.example.client.Activities.EventsActivity;
import com.example.client.Activities.GuestsActivity;
import com.example.client.Activities.LoginActivity;
import com.example.client.Activities.MainActivity;
import com.example.client.Activities.RegisterActivity;
import com.example.client.Callback.RequestCallback;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.R;
import com.example.client.Util.Connection.AuthManager;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.Connection.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationHelper {

    public static void navigate(Activity activity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                navigateToActivity(activity, MainActivity.class);
                break;
            case R.id.nav_create:
                navigateToActivity(activity, CreateEventActivity.class);
                break;
            case R.id.nav_login:
                navigateToActivity(activity, LoginActivity.class);
                break;
            case R.id.nav_signUp:
                navigateToActivity(activity, RegisterActivity.class);
                break;
            case R.id.nav_guest:
                navigateToActivity(activity, GuestsActivity.class);
                break;
            case R.id.nav_events:
                navigateToActivity(activity, EventsActivity.class);
                break;
            case R.id.nav_logout: {
                // Handle logout
                MySharedPrefManager.getInstance().setSharedPref(activity,false);
                RetrofitClient.clearCookies();
                Call<MyResponse> call= ConnectionManager.apiService.getLogout();
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        Log.i("RESPONSE_LOGOUT",response.code()+" "+response.message());
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {

                    }
                });
            }
            AuthManager.clearJWT(activity);
            Toast.makeText(activity, "Logged Out", Toast.LENGTH_SHORT).show();
            navigateToActivity(activity , LoginActivity.class);
                break;
        }

    }

    private static void navigateToActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }
}
