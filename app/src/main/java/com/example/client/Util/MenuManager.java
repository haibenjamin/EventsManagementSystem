package com.example.client.Util;

import android.view.Menu;
import android.view.MenuItem;

import com.example.client.R;
import com.google.android.material.navigation.NavigationView;

public class MenuManager {

    private static MenuManager instance;
    public MenuItem logoutItem;
    public MenuItem loginItem;
    public NavigationView navigationView;
    public Menu navMenu;
    public boolean[] visibility;


    public MenuManager() {

        int size=3;
        visibility=new boolean[size];
        for (int i=0;i<size;i++){
            visibility[i]=true;
        }
        visibility[0]=true;

    }



    public static MenuManager getInstance() {
        if (instance == null) {
            instance = new MenuManager();
        }
        return instance;
    }
    public void setNavigationView(NavigationView nv){
        navigationView=nv;
    }
    public void setMenu(Menu m){
        navMenu=m;

    }
    public Menu getNavMenu(){
        return this.navMenu;
    }

    public void setLogoutItem(MenuItem item) {
        this.logoutItem = item;
    }

    public MenuItem getLogoutItem() {
        return logoutItem;
    }

    public void setLoginItem(MenuItem item) {
        this.loginItem = item;
    }

    public MenuItem getLoginItem() {
        return loginItem;
    }

    public void setVisibility(ItemsEnum.itemsEnum pos, boolean state) {
        visibility[pos.ordinal()]=state;
    }
    public boolean[] getVisibility(){return visibility;}

}
