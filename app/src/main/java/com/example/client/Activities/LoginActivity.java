package com.example.client.Activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client.Callback.BtnVisCallBack;
import com.example.client.Callback.GuestCallBack;
import com.example.client.Callback.RequestCallback;
import com.example.client.R;
import com.example.client.Util.Connection.AuthManager;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.ItemsEnum;
import com.example.client.Util.MySharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button signInBtn, signUpBtn;
    EditText nameET, passwordET;
    private String name, password;
    private NavigationView navView;

    ItemsEnum items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupDrawer();
        findViews();

        navView.setNavigationItemSelectedListener(this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameET.getText().toString();
                password = passwordET.getText().toString();
                JSONObject json = new JSONObject();
                try {
                    json.put("username", name);
                    json.put("password", password);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson = new Gson();

                ConnectionManager.addPathToRoot("/login");
                ConnectionManager.postRequest(json.toString(), new RequestCallback() {
                    @Override
                    public void onResponse(int code, String message) {
                        if (code == 200) {
                            Log.i("RESPONSE", message);
                            String accesstoken = getAccessTokenFromMessage(message);
                            //AuthManager.getInstance().setAccessToken(getAccessTokenFromMessage(message));
                            AuthManager.getInstance().setJWT(accesstoken);
                            AuthManager.getInstance().setRole();
                            ConnectionManager.initRetrofit();
                            MySharedPrefManager.getInstance().setSharedPref(LoginActivity.this, true);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "logged in succesfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private String getAccessTokenFromMessage(String message) {
        String accessToken = "";
        Gson g = new Gson();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if (jsonObject.has("accessToken"))
            accessToken = jsonObject.get("accessToken").getAsString();

        return accessToken;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return super.onNavigationItemSelected(item);
    }

    private void findViews() {
        signInBtn = findViewById(R.id.login_signInBtn);
        signUpBtn = findViewById(R.id.login_signUpBtn);
        nameET = findViewById(R.id.login_name_ET);
        passwordET = findViewById(R.id.login_password_ET);
        navView = findViewById(R.id.nav_view);

    }


}