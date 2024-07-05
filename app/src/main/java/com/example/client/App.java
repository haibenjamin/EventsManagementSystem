package com.example.client;

import android.app.Application;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.client.Util.ImageLoader;

public class App extends Application {
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.initImageLoader(this);
    }
}
