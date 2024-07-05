package com.example.client.Model.Responses;

import com.example.client.Model.SugVendor;

import java.util.ArrayList;

public class VendorResponse {
    ArrayList<SugVendor> suggestedVendors;
    ArrayList<SugVendor> addedVendors;

    public ArrayList<SugVendor> getAddedVendors() {
        return addedVendors;
    }

    public void setAddedVendors(ArrayList<SugVendor> addedVendors) {
        this.addedVendors = addedVendors;
    }

    public ArrayList<SugVendor> getNegotiatedVendors() {
        return negotiatedVendors;
    }

    public void setNegotiatedVendors(ArrayList<SugVendor> negotiatedVendors) {
        this.negotiatedVendors = negotiatedVendors;
    }

    ArrayList<SugVendor> negotiatedVendors;

    public ArrayList<SugVendor> getSuggestedVendors() {
        return suggestedVendors;
    }

    public void setSuggestedVendors(ArrayList<SugVendor> suggestedVendors) {
        this.suggestedVendors = suggestedVendors;
    }

    @Override
    public String toString() {
        return "VendorResponse{" +
                "suggestedVendors=" + suggestedVendors +
                '}';
    }
}