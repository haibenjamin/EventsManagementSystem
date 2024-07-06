package com.example.client.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.Adapters.GuestAdapter;
import com.example.client.Callback.GuestCallBack;
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.Responses.DeleteGuestRequest;
import com.example.client.Model.Responses.ErrorResponse;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.Model.Responses.GuestResponse;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    Button addBtn;
    EditText nameET, phoneET;
    RecyclerView guestList;
    GuestAdapter guestAdapter;
    Spinner dropDown;
    private NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guests);
        getGuestsFromServer();
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);
        initViews();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddContactDialog();

            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                DataManager.deleteGuest(position);
                guestAdapter.notifyDataSetChanged();
            }
        };
    }

    private void getGuestsFromServer() {
        String userId = ConnectionManager.getUserId();
        Call<GuestResponse> call = ConnectionManager.apiService.getGuests(userId, ConnectionManager.getSelectedEventId());
        call.enqueue(new Callback<GuestResponse>() {
            @Override
            public void onResponse(Call<GuestResponse> call, Response<GuestResponse> response) {
                if (response.isSuccessful()) {
                    GuestResponse guestResponse = response.body();
                    if (guestResponse != null) {
                        ArrayList<Guest> guests = guestResponse.getGuests();
                        DataManager.setGuests(guests);
                        initViews();
                    }
                }
            }

            @Override
            public void onFailure(Call<GuestResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initViews() {
        //DataManager.initList();
        guestAdapter = new GuestAdapter(DataManager.getGuests());
        updateRecyclerView(guestAdapter);
    }

    private void updateRecyclerView(GuestAdapter guestAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        guestList.setAdapter(guestAdapter);
        guestList.setLayoutManager(linearLayoutManager);
        guestAdapter.setGuestCallback(new GuestCallBack() {
            @Override
            public void deleteClicked(Guest guest, int position) {

                ArrayList<String> selectedGuestsIDs = new ArrayList<>();
                selectedGuestsIDs.add(guest.get_id());
                Map<String, ArrayList<String>> requestBody = new HashMap<>();
                requestBody.put("selectedGuestsIDs", selectedGuestsIDs);
                String userId = ConnectionManager.getUserId();
                String eventID = ConnectionManager.getSelectedEventId();
                Call<EventResponse> call = ConnectionManager.apiService.deleteGuests(userId, eventID, requestBody);
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        if (response.isSuccessful()) {
                            DataManager.deleteGuest(position);
                            guestList.getAdapter().notifyDataSetChanged();
                            Toast.makeText(GuestsActivity.this,"Task Deleted Successfully",Toast.LENGTH_SHORT).show();


                        } else {
                            Log.i("DELETE GUEST FAILED", response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        Log.i("FAILED", t.getMessage());
                    }
                });
            }

            @Override
            public void statusClicked(Guest guest, int position) {
                showEditStatusDialog(guest);
            }

            @Override
            public void guestsClicked(Guest guest, int position) {
                showAlertDialog(guest, "people count");
            }

            @Override
            public void commentClicked(Guest guest, int position) {
                // showAlertDialog(guest,"comments");
            }
        });
    }


    private void showAlertDialog(Guest guest, String title) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.edit_guest_dialog, null);
        EditText contentET = dialogLayout.findViewById(R.id.guest_dialog_et);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("Change " + title);
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Override the positive button click listener to perform validation
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String input = contentET.getText().toString().trim();
            if (input.isEmpty())
                Toast.makeText(GuestsActivity.this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
            else if (!ConnectionManager.isNumeric(input) && title.equals("people count"))
                Toast.makeText(GuestsActivity.this, title + " must be a number", Toast.LENGTH_SHORT).show();
            else {
                // Input is valid, dismiss the dialog
                dialog.dismiss();
                // Handle the input
                handleInput(input, contentET, title, guest);
            }
        });
    }

    private void handleInput(String input, EditText contentET, String title, Guest guest) {
        String content = contentET.getText().toString();
        if (!content.isEmpty())
            switch (title) {

                case "people count": {
                    updateGuestsNumber(guest, content);
                }
                break;
                case "group": {
                    updateGuestGroup(guest, content);
                }
                break;
                case "phone": {
                    updateGuestPhone(guest, content);
                }
                break;
                case "comments": {
                    updateGuestComments(guest, content);
                }
                break;
            }
        else {
            Toast.makeText(GuestsActivity.this, "Please enter " + title, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGuestComments(Guest guest, String content) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("comments", content);
        Call<MyResponse> call = ConnectionManager.apiService.updateComment(ConnectionManager.getUserId(),
                ConnectionManager.getSelectedEventId(), guest.get_id(), jsonObject);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GuestsActivity.this, "comment updated successfully", Toast.LENGTH_SHORT).show();
                    DataManager.updateComment(guest, content);
                    initViews();
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void updateGuestPhone(Guest guest, String content) {
    }

    private void updateGuestGroup(Guest guest, String content) {
    }

    private void updateGuestsNumber(Guest guest, String numStr) {
        JsonObject num = new JsonObject();
        num.addProperty("peopleCount", numStr);
        Call<MyResponse> call = ConnectionManager.apiService.updateStatus(ConnectionManager.getUserId(),
                ConnectionManager.getSelectedEventId(), guest.get_id(), num);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GuestsActivity.this, "number of guests updated successfully", Toast.LENGTH_SHORT).show();
                    DataManager.updateNumOfGuests(guest, Integer.valueOf(numStr));
                    initViews();
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void showEditStatusDialog(Guest guest) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.edit_status_dialog, null);
        // Initialize views

        Spinner spinnerStatus = dialogLayout.findViewById(R.id.guest_dialog_status_dd);


        //initialize dropdown
        String[] items = new String[]{"Message Sent", "Coming", "Not Coming", "Maybe"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        spinnerStatus.setAdapter(adapter);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("Change Status");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the OK button click
                try {
                    updateStatus(guest, spinnerStatus.getSelectedItem().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //show dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateStatus(Guest guest, String selectedStatus) throws JSONException {
        JsonObject status = new JsonObject();
        status.addProperty("status", selectedStatus);
        Call<MyResponse> call = ConnectionManager.apiService.updateStatus(ConnectionManager.getUserId(),
                ConnectionManager.getSelectedEventId(), guest.get_id(), status);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    DataManager.updateStatus(guest, selectedStatus);
                    initViews();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showAddContactDialog() {
        // Create a dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_guest_cutom_dialog);

        // Initialize views
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextPhoneNumber = dialog.findViewById(R.id.editTextPhoneNumber);
        Spinner spinnerStatus = dialog.findViewById(R.id.spinnerStatus);
        EditText editTextComments = dialog.findViewById(R.id.editTextComments);
        EditText editTextGroup = dialog.findViewById(R.id.editTextGroup);
        EditText editTextGuests = dialog.findViewById(R.id.editTextGuests);
        Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);

        //initialize dropdown
        String[] items = new String[]{"Message Sent", "Coming", "Not Coming", "Maybe"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        spinnerStatus.setAdapter(adapter);

        // Set up the "Add" button click listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();
                String status = spinnerStatus.getSelectedItem().toString();
                String comments = editTextComments.getText().toString();
                String group = editTextGroup.getText().toString();
                String guests = editTextGuests.getText().toString();
                String formattedPhoneNum;
                int peopleCount = 0;
                if (ConnectionManager.isNumeric(guests)) {
                    peopleCount = Integer.parseInt(guests);
                }
                if (verifyPhoneNumber(phoneNumber)) {
                    formattedPhoneNum = formatPhoneNumber(phoneNumber);
                    Guest newGuest = new Guest(name, formattedPhoneNum, peopleCount, group, status, comments);
                    Call<MyResponse> call = ConnectionManager.apiService.addGuest(ConnectionManager.getUserId(), ConnectionManager.getSelectedEventId(), newGuest);
                    call.enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(GuestsActivity.this,"Guest Added Successfully",Toast.LENGTH_SHORT).show();
                                DataManager.addGuest(newGuest);
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);

                                dialog.dismiss();
                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Gson gson = new Gson();
                                    Type errorResponseType = new TypeToken<ErrorResponse>() {
                                    }.getType();
                                    ErrorResponse errorResponse = gson.fromJson(errorBody, errorResponseType);
                                    if (errorResponse.getErrors() != null) {
                                        Toast.makeText(GuestsActivity.this, errorResponse.getErrors().get(0).getMsg(), Toast.LENGTH_LONG).show();

                                        // Log or handle the errors
                                        for (ErrorResponse.Error error : errorResponse.getErrors()) {
                                            Log.i("ERROR", "Type: " + error.getType() + ", Message: " + error.getMsg());
                                        }
                                    } else {
                                        JsonObject jsonObject = JsonParser.parseString(errorBody).getAsJsonObject();
                                        String errorMessage = jsonObject.get("err").getAsString();
                                        Toast.makeText(GuestsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.i("ADD GUEST FAILED", t.getMessage());
                        }
                    });
                }
            }
        });

        // Set up the "Cancel" button click listener
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    private boolean verifyPhoneNumber(String phoneNumber) {
        // Check if the phone number is null or has an incorrect length
        if (phoneNumber == null || phoneNumber.length() != 10) {
            Toast.makeText(GuestsActivity.this, "Phone number must be 10 digits long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public String formatPhoneNumber(String phoneNumber) {

        // Split the phone number into parts
        String part1 = phoneNumber.substring(0, 3);
        String part2 = phoneNumber.substring(3, 6);
        String part3 = phoneNumber.substring(6);

        // Combine the parts with dashes
        return part1 + "-" + part2 + part3;
    }
    private void setGuests(ArrayList<Guest> guests) {
        DataManager.setGuests(guests);
        initViews();
    }
    private void findViews() {
        addBtn = findViewById(R.id.guest_add_btn);
        guestList = findViewById(R.id.guest_list_rv);
        navView = findViewById(R.id.nav_view);
    }
}