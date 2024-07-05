package com.example.client.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.client.Util.Connection.AuthManager;

public class MySharedPrefManager {
    private static MySharedPrefManager instance;

    private MySharedPrefManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MySharedPrefManager getInstance() {
        if (instance == null) {
            instance = new MySharedPrefManager();
        }
        return instance;
    }

    public void setSharedPref(Context context, boolean state) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("EventPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", state);
        AuthManager.getInstance().setIsConnected(state);
        editor.apply();
    }

    public boolean getSharedPref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("EventPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
}
