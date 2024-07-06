package com.example.client.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client.Adapters.NegVendorAdapter;
import com.example.client.Adapters.SugTaskAdapter;
import com.example.client.Adapters.SugVendorAdapter;
import com.example.client.Adapters.VendorAdapter;
import com.example.client.Callback.NegVendorCallBack;
import com.example.client.Callback.SugVendorCallBack;
import com.example.client.Callback.VendorCallBack;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Model.Responses.VendorResponse;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Task;
import com.example.client.R;
import com.example.client.Util.Connection.ConnectionManager;
import com.example.client.Util.DataManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navView;
    RecyclerView addedList,sugList,negList;
    Button sugBtn,negBtn;
    private Dialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);
        getVendorsFromServer();
        setupDrawer();
        findViews();
        navView.setNavigationItemSelectedListener(this);
        initViews();
        sugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuggestedVendorsDialog();
            }
        });
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNegotiatedVendorsDialog();
            }
        });
    }

    private void showNegotiatedVendorsDialog() {

        NegVendorAdapter negVendorAdapter = new NegVendorAdapter(DataManager.getNegotiatedVendors(),VendorAdapter.NEGOTIATED_VENDORS);

// Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.sugg_vendors_dialog, null);

// Find RecyclerView inside the inflated dialog layout
        negList = dialogLayout.findViewById(R.id.dialog_suggested_rv);
        updateRecyclerView(negVendorAdapter, negList);

// Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("Negotiated Tasks");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateRecyclerView(NegVendorAdapter negVendorAdapter, RecyclerView negList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        negList.setAdapter(negVendorAdapter);
        negList.setLayoutManager(linearLayoutManager);
        negVendorAdapter.setNegVendorCallBack(new NegVendorCallBack() {
            @Override
            public void acceptClicked(SugVendor vendor, int position) {
                askForPrice(vendor);


            }

            @Override
            public void declineClicked(SugVendor vendor, int position) {
                  deleteFromNegotiated(vendor);
                  updateRecyclerView(negVendorAdapter,negList);
                    addToSuggested(vendor);
            }
        });
    }

    private void askForPrice(SugVendor vendor) {
        showAlertDialog(vendor);
    }

    private void showAlertDialog(SugVendor vendor) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_price_dialog, null);

        //init views
        final EditText priceTxt = dialogLayout.findViewById(R.id.dialog_price_et);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("Add Price");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the OK button click
                int price;
                price=verifyPrice(priceTxt.getText().toString());
                if (price>0) {
                    DataManager.setPrice(vendor,price);
                    MoveToAddedVendors(vendor, price);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int verifyPrice(String priceStr) {
        if (!priceStr.equals("")) {
            if (ConnectionManager.isNumeric(priceStr)) {
                return Integer.parseInt(priceStr);
            }
            else{
                Toast.makeText(VendorsActivity.this, "price must be a number", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        else {
            Toast.makeText(VendorsActivity.this, "please enter the given price by the vendor", Toast.LENGTH_SHORT).show();
            return 0;
        }

    }

    private void MoveToAddedVendors(SugVendor vendor,int price) {
        JsonObject json = new JsonObject();
        json.addProperty("priceForService",price);
        Call<MyResponse> call = ConnectionManager.apiService.addToMyVendors(ConnectionManager.getUserId(),ConnectionManager.getSelectedEventId(),vendor.get_id(),json);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful())
                {
                    DataManager.addVendor(vendor);
                    deleteFromNegotiated(vendor);
                    //reload the rv after deletion
                    updateRecyclerView(new NegVendorAdapter(DataManager.getNegotiatedVendors(),NegVendorAdapter.NEGOTIATED_VENDORS),negList);
                    //reload my vendors rv after adding
                    setupRecyclerView(addedList,new VendorAdapter(DataManager.getAddedVendors(), VendorAdapter.ADDED_VENDORS));

                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void addToSuggested(SugVendor vendor) {
        DataManager.addToSuggested(vendor);
        // addToNegotiatedServer(vendor);
    }

    private void deleteFromNegotiated(SugVendor vendor) {
        DataManager.deleteNegVendor(vendor);
        // deleteVendorFromServer(vendor);
    }

    private void updateRecyclerView(SugVendorAdapter vendorAdapter, RecyclerView list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        list.setAdapter(vendorAdapter);
        list.setLayoutManager(linearLayoutManager);
        vendorAdapter.setVendorCallBack(new SugVendorCallBack() {
            @Override
            public void contactClicked(SugVendor vendor, int position) {
                contactVendor(vendor);
                showProgressBar();
            }
        });
    }

    private void showProgressBar() {
        progressDialog = new Dialog(VendorsActivity.this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    private void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showSuggestedVendorsDialog() {
        SugVendorAdapter vendorAdapter = new SugVendorAdapter(DataManager.getSuggestedVendors(),VendorAdapter.SUGGESTED_VENDORS);

// Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.sugg_vendors_dialog, null);

// Find RecyclerView inside the inflated dialog layout
        sugList = dialogLayout.findViewById(R.id.dialog_suggested_rv);
        updateRecyclerView(vendorAdapter, sugList);

// Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setTitle("Suggested Tasks");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

// Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setupRecyclerView(RecyclerView recyclerView, VendorAdapter vendorAdapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(vendorAdapter);
        recyclerView.setLayoutManager(layoutManager);
        vendorAdapter.setVendorCallBack(new VendorCallBack() {

            @Override
            public void deleteClicked(SugVendor vendor, int position) {
                deleteVendor(vendor);
            }
        });
    }

    private void addToNegotiated(SugVendor vendor) {
        DataManager.addToNegotiated(vendor);
        // addToNegotiatedServer(vendor);
    }

    private void deleteFromSuggested(SugVendor vendor) {
        DataManager.deleteSuggVendor(vendor);
        // deleteVendorFromServer(vendor);
    }

    private void contactVendor(SugVendor vendor) {
        Call<MyResponse> call = ConnectionManager.apiService.contactVendor
                (ConnectionManager.getUserId(),ConnectionManager.getSelectedEventId(),vendor.get_id());
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful())
                {
                    deleteFromSuggested(vendor);
                    SugVendorAdapter vendorAdapter = new SugVendorAdapter(DataManager.getSuggestedVendors(),VendorAdapter.SUGGESTED_VENDORS);
                    hideProgressBar();
                    updateRecyclerView(vendorAdapter,sugList);
                    addToNegotiated(vendor);
                }
                else{
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void deleteVendor(SugVendor vendor) {
        DataManager.deleteAddedVendor(vendor);
        deleteVendorFromServer(vendor);
    }

    private void deleteVendorFromServer(SugVendor vendor) {
        Gson gson = new Gson();
        JsonObject vendorJson = new JsonObject();
        vendorJson.add("vendor", gson.toJsonTree(vendor));

        Call<MyResponse> call = ConnectionManager.apiService.deleteVendor(ConnectionManager.getUserId(),ConnectionManager.getSelectedEventId() ,vendorJson);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.isSuccessful()) {
                    deleteFromSuggested(vendor);
                    setupRecyclerView(addedList,new VendorAdapter(DataManager.getAddedVendors(),VendorAdapter.ADDED_VENDORS));
                } else {
                    // Handle failure
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
    private void initViews() {
        VendorAdapter vendorAdapter = new VendorAdapter(DataManager.getAddedVendors(), VendorAdapter.ADDED_VENDORS);
        setupRecyclerView(addedList, vendorAdapter);
    }

    private void findViews() {
        sugBtn = findViewById(R.id.vendors_sugg_btn);
        negBtn=findViewById(R.id.vendors_neg_btn);
        navView = findViewById(R.id.nav_view);
        addedList = findViewById(R.id.vendors_list_rv);
    }

    private void getVendorsFromServer() {
        String userId = ConnectionManager.getUserId();
        Call<VendorResponse> call = ConnectionManager.apiService.getVendors(userId, ConnectionManager.getSelectedEventId());
        call.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("ADDED VENDORS", response.body().toString());
                    VendorResponse vendorResponse = response.body();
                    if (vendorResponse != null) {
                        ArrayList<SugVendor> addedVendors = vendorResponse.getAddedVendors();
                        ArrayList<SugVendor> suggestedVendors = vendorResponse.getSuggestedVendors();
                        ArrayList<SugVendor> negotiatedVendors = vendorResponse.getNegotiatedVendors();
                        DataManager.setVendors(addedVendors, suggestedVendors, negotiatedVendors);
                        initViews();
                    }
                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
