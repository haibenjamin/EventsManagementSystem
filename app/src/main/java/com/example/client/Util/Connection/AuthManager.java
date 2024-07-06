package com.example.client.Util.Connection;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.client.Model.UserInfo;

public class AuthManager {

    private String accessToken,role,name;
    private boolean isConneted;
    private static AuthManager instance;
    private JWT jwt;

    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public String getRole() {
            return role;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken(){
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken=accessToken;
    }

    public boolean getIsConnected() {
        return isConneted;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConneted = isConnected;
    }



    public void setJWT(String accessToken){
        this.accessToken=accessToken;
         jwt = new JWT(accessToken);
    }

        public static void clearJWT(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("jwt_token");
            editor.apply();
        }

    public String getUserId(){
        if (jwt!=null){
            Claim claim = jwt.getClaim("userInfo");
            return  claim.asObject(UserInfo.class).getId();
        }
        return null;

    }


    public void setRole() {
        if (jwt!=null){
            Claim claim = jwt.getClaim("userInfo");
            role= claim.asObject(UserInfo.class).getRole();
        }
    }
}
