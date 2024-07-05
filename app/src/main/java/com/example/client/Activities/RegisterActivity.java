package com.example.client.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.client.Callback.RequestCallback;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

enum Type{
    EVENT_PLANNER,VENDOR,GUEST
}
public class RegisterActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText nameView;
    EditText emailView;
    EditText passwordView;
    Spinner dropdown;
    Button signBtn;
    NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);

        String[] items = new String[] {"Event Planner","Vendor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        dropdown.setAdapter(adapter);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,password,email;
                int chosen;
                String data2="{\"username\": \"hai2\",\"email\": \"hai2@gmail.com\",\"password\": \"12345aA\",\"role\": \"Event Planner\"}";
                name=nameView.getText().toString();
                password=passwordView.getText().toString();
                email=emailView.getText().toString();
                chosen=dropdown.getSelectedItemPosition();
                Log.d("",name +" "+ password+" "+ email+" "+chosen);
                JSONObject json = new JSONObject();
                try {
                    json.put("username",name);
                    json.put("email",email);
                    json.put("password",password);
                    json.put("role",items[chosen]);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                Gson gson= new Gson();

                ConnectionManager.addPathToRoot("/register");
                ConnectionManager.postRequest(json.toString(), new RequestCallback() {
                    @Override
                    public void onResponse(int code, String message) {
                        if(code== HttpURLConnection.HTTP_CREATED) {
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            Toast.makeText(RegisterActivity.this, "signed up succesfully", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }

    private void findViews() {
        nameView=findViewById(R.id.reg_name_et);
        passwordView=findViewById(R.id.reg_pw_et);
        emailView=findViewById(R.id.reg_email_et);
        dropdown=findViewById(R.id.reg_type_dd);
        signBtn=findViewById(R.id.reg_sign_btn);
        navView=findViewById(R.id.nav_view);
    }
}