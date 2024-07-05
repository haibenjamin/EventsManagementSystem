package com.example.client.Model.Responses;

import java.util.ArrayList;
import java.util.List;

public class DeleteGuestRequest {
    private ArrayList<String> selectedGuestIDs;

    public DeleteGuestRequest(ArrayList<String> selectedGuestIDs){
        this.selectedGuestIDs=selectedGuestIDs;
    }
    public ArrayList<String> getSelectedGuestIDs(){
        return selectedGuestIDs;
    }
    public void setSelectedGuestIDs(ArrayList<String> selectedGuestIDs){
        this.selectedGuestIDs=selectedGuestIDs;
    }
}
