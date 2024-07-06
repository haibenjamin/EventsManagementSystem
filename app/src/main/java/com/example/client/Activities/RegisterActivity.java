package com.example.client.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.client.Callback.RequestCallback;
import com.example.client.Model.Guest;
import com.example.client.Model.Responses.ErrorResponse;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Model.SugVendor;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

enum Type{
    EVENT_PLANNER,VENDOR,GUEST
}
public class RegisterActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText nameView,emailView,passwordView;
    String businesseType,businessName,description;
    ArrayList<String> selectedLocations,selectedTypes;
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
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = items[i];
                if (selectedItem.equals("Vendor")) {
                    showAlertDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case when nothing is selected
            }
        });


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,password,email;
                int chosen;

                name=nameView.getText().toString();
                password=passwordView.getText().toString();
                email=emailView.getText().toString();
                chosen=dropdown.getSelectedItemPosition();
                JSONObject json = new JSONObject();
                try {
                    json.put("username",name);
                    json.put("email",email);
                    json.put("password",password);
                    json.put("role",items[chosen]);
                    if (items[chosen].equals("Vendor")){
                        json.put("businessName",businessName);
                        json.put("businessType",businesseType);
                        JSONArray locationArray = new JSONArray(selectedLocations);
                        json.put("businessLocation", locationArray);
                        JSONArray typeArray = new JSONArray(selectedTypes);
                        json.put("businessTypes", typeArray);
                        json.put("businessDescription",description);
                    }
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

    private void showAlertDialog() {
        // Create a dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_vendor_dialog);

        // Initialize views
        EditText businessNameET = dialog.findViewById(R.id.dialog_name_et);
        EditText businessTypeET = dialog.findViewById(R.id.dialog_type_et);
        EditText descriptionET = dialog.findViewById(R.id.dialog_description_et);
        ListView locationList = dialog.findViewById(R.id.dialog_location_list);
        ListView typeList = dialog.findViewById(R.id.dialog_type_list);
        Button buttonAdd = dialog.findViewById(R.id.dialog_add_btn);
        Button buttonCancel = dialog.findViewById(R.id.dialog_cancel_btn);

        //initialize checked text view
        String[] typeItems = new String[]{"Birthday", "Wedding", "Conference", "Company Event", "Bar/Bat Mitzva"};
        String[] locationItems = new String[]{"Haifa & North", "Hasharon", "Gush Dan", "Shfela", "Jerusalem", "South(Negev And Eilat)"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, locationItems);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, typeItems);
        locationList.setAdapter(locationAdapter);
        typeList.setAdapter(typeAdapter);

        // Set up the "Add" button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = businessNameET.getText().toString();
                String type = businessTypeET.getText().toString();
                String desc = descriptionET.getText().toString();
                for (int i = 0; i < locationList.getCount(); i++) {
                    if (locationList.isItemChecked(i))
                        selectedLocations.add(locationItems[i]);
                    if (typeList.isItemChecked(i))
                        selectedTypes.add(typeItems[i]);
                }
                if (verifyInputs(name, type, selectedLocations, selectedTypes, desc)) {
                    businessName = name;
                    businesseType = type;
                    description = desc;
                    dialog.dismiss();
                }


                // Set up the "Cancel" button click listener
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private boolean verifyInputs(String name, String type, ArrayList<String> selectedLocations, ArrayList<String> selectedTypes, String desc) {
        if (name.isEmpty())
            return false;
        return true;
    }


    private void findViews() {
        nameView=findViewById(R.id.reg_name_et);
        passwordView=findViewById(R.id.reg_pw_et);
        emailView=findViewById(R.id.reg_email_et);
        dropdown=findViewById(R.id.reg_type_dd);
        signBtn=findViewById(R.id.reg_sign_btn);
        navView=findViewById(R.id.nav_view);
        selectedLocations=new ArrayList<>();
        selectedTypes=new ArrayList<>();
    }
}



