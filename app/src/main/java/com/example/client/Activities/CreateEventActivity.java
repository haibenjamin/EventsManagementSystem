package com.example.client.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.Callback.DatePickerCallback;
import com.example.client.Model.Collaborator;
import com.example.client.Model.EventData;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.R;
import com.example.client.Util.Connection.AuthManager;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.MyDate;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button createBTN,selectDateBTN;
    EditText nameET,dateET,budgetET;
    Spinner typeDropdown,locationDropdown;
    TextView selectedDateTxt;
    long selectedDate=-1;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check if logged in
        if(!AuthManager.getInstance().getIsConnected())
            startActivity(new Intent(CreateEventActivity.this,LoginActivity.class));

        setContentView(R.layout.activity_create_event);
        setupDrawer();
        findViews();
        selectDateBTN.setOnClickListener(v -> MyDate.showDatePickerDialog(this, new DatePickerCallback() {
            @Override
            public void onDateSelected(long epochTime) {
                selectedDate=epochTime;
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(epochTime * 1000L);
                selectedDateTxt.setText(formattedDate);
            }
        }));
        navView.setNavigationItemSelectedListener(this);
       initRecyclerViews();
        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String userId = ConnectionManager.getUserId(); // Your function to decode JWT and extract userId
                    String name="",location="",dateStr="",budgetStr="",type="";
                    ArrayList<Collaborator> collab = new ArrayList<>();
                    int budget=0;

                    //getInputs
                     name=nameET.getText().toString();
                     location=locationDropdown.getSelectedItem().toString();
                     budgetStr=budgetET.getText().toString();
                     type=typeDropdown.getSelectedItem().toString();

                    if( validateInput(name,location,budgetStr,type,selectedDate)) {
                        budget=Integer.parseInt(budgetStr);
                        EventData eventData = new EventData(name, budget, selectedDate, location, type, collab);
                        postEvent(userId, eventData);
                    }
            }
        });
            }


    private void initRecyclerViews() {
        String[] typeItems = new String[] {"Birthday","Wedding","Conference","Company Event","Bar/Bat Mitzva"};
        String[] locationItems = new String[] {"Haifa & North","Hasharon","Gush Dan","Shfela","Jerusalem","South(Negev And Eilat)"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,typeItems);
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,locationItems);
        typeDropdown.setAdapter(typeAdapter);
        locationDropdown.setAdapter(locationAdapter);
    }

    private boolean validateInput(String name, String location, String budgetStr,String type,long date) {


        if (name.equals("")) {
            Log.i("CREATE EVENT INPUT",name);
            Toast.makeText(CreateEventActivity.this, "Please enter a name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (location.equals("")) {
            Log.i("CREATE EVENT INPUT",location);
            Toast.makeText(CreateEventActivity.this, "Please enter a location", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (budgetStr.equals("")) {
            Log.i("CREATE EVENT INPUT",budgetStr);
            Toast.makeText(CreateEventActivity.this, "Please enter a budget", Toast.LENGTH_LONG).show();
            return false;
        }else if (!ConnectionManager.isNumeric(budgetStr)) {
            Log.i("CREATE EVENT INPUT",budgetStr);
            Toast.makeText(CreateEventActivity.this, "budget must be a number", Toast.LENGTH_LONG).show();
            return false;
        }

        else if (date<System.currentTimeMillis()/1000) {
            Log.i("CREATE EVENT INPUT",""+date+" "+System.currentTimeMillis());
            Toast.makeText(CreateEventActivity.this, "date isn't valid", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;

    }


    private void postEvent(String userId, EventData eventData) {
        Call<EventResponse> call = ConnectionManager.apiService.postEvent(userId, eventData);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                   Toast.makeText(CreateEventActivity.this,"Event "+nameET.getText().toString()+" Created Succssesfully",Toast.LENGTH_LONG).show();
                   startActivity(new Intent(CreateEventActivity.this,EventsActivity.class));
                } else {

                    try {
                        if(response.errorBody()!=null) {
                            String msg = ConnectionManager.identifyMessage(response.errorBody().string());
                            Toast.makeText(CreateEventActivity.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
        createBTN = findViewById(R.id.event_create_btn);
        nameET=findViewById(R.id.event_name_et);
        budgetET=findViewById(R.id.event_budget_et);
        locationDropdown=findViewById(R.id.event_location_dd);
        typeDropdown=findViewById(R.id.event_type_dd);
        navView=findViewById(R.id.nav_view);
        selectDateBTN=findViewById(R.id.event_select_date_btn);
        selectedDateTxt=findViewById(R.id.event_selected_date_txt);
    }
}
