package com.example.client.Util;
import android.util.Log;

import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Task;
import com.example.client.Model.Vendor;
import com.example.client.Model.sugTask;

import java.util.ArrayList;
import java.util.List;

public  class DataManager {
    static ArrayList<Guest> guests;
    static ArrayList<EventData> events;
    static ArrayList<Task> tasks;
    static ArrayList<sugTask> suggestedTasks;
    static ArrayList<SugVendor> suggestedVendors;
    static ArrayList<SugVendor> addedVendors;
    static ArrayList<SugVendor> negotiatedVendors;
   static String[] collab = {"hai2@gmail.com"};

    public static void initList(){
        guests= new ArrayList<>();
        events=new ArrayList<>();
        tasks=new ArrayList<>();
    }
    public static ArrayList<Guest> getGuests() {




        return guests;
    }
    public static ArrayList<EventData> getEvents() {
        return events;
    }
    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static ArrayList<Task> getTaskByColumn(String column) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        if(tasks!=null)
            for (Task task : tasks) {
            if (task.getColumn().equals( column)) {
                filteredTasks.add(task);
                Log.i("TASK",task.getColumn()+"Added , selected="+task.getSelected() );
            }

        }

        return filteredTasks;
    }

    public static void addGuest(Guest newGuest){
        guests.add(newGuest);

    }
    public static void deleteGuest(int position){
        guests.remove(position);
    }
    public static void deleteEvent(int position){
        events.remove(position);
    }

    public static void setEvents(ArrayList<EventData> events) {
        DataManager.events=events;
    }
    public static void convertEpochToFormmatedDate(){
        for (EventData event : events) {
            event.convertEpochToFormattedDate();
        }
    }
    public static String getEventId(int pos){
        return events.get(pos).getId();
    }

    public static void setGuests(ArrayList<Guest> guestList) {
        guests=guestList;
    }

    public static void setTasks(ArrayList<Task> taskList,ArrayList<sugTask> sugTasks) {
        tasks=taskList;
        suggestedTasks=sugTasks;
    }

    public static ArrayList<Task> getSelectedTasks() {
        ArrayList<Task> selected=new ArrayList<>();
        for (Task task :tasks){
            if (task.getSelected())
                selected.add(task);
        }
        return selected;
    }
    public static void addTask(Task newTask){
        tasks.add(newTask);

    }

    public static ArrayList<sugTask> getSuggestedTasks() {
        return suggestedTasks;
    }

    public static void setSuggestedTasks(ArrayList<sugTask> suggestedTasks) {
        DataManager.suggestedTasks = suggestedTasks;
    }
    public static ArrayList<String> getSuggestedTasksToString(){
        ArrayList<String> tasks = new ArrayList<>();
        for(sugTask task : suggestedTasks){
            for (String taskStr : task.getTasks()){
                tasks.add(taskStr);
            }
        }
        return tasks;

    }

    public static void deleteSuggTask(String selectedTask) {
        for (sugTask task : suggestedTasks) {
            for (String taskStr : task.getTasks()) {
                if (taskStr.equals(selectedTask)) {
                    task.remove(selectedTask);
                    Task newTask = new Task(selectedTask,"Backlog");
                    addTask(newTask);
                    break;
                }

            }
        }
    }

    public static void updateStatus(Guest selectedGuest, String selectedStatus) {
        for (Guest guest:guests )
            if (guest.equals(selectedGuest))
                guest.setStatus(selectedStatus);
    }

    public static void updateNumOfGuests(Guest selectedGuest, int num) {
        for (Guest guest:guests )
            if (guest.equals(selectedGuest))
                guest.setPeopleCount(num);
    }

    public static void updateComment(Guest selectedGuest, String content) {
        for (Guest guest:guests )
            if (guest.equals(selectedGuest))
                guest.setStatus(content);
    }

    public static ArrayList<SugVendor> getSuggestedVendors() {
        return DataManager.suggestedVendors;
    }

    public static void setVendors(ArrayList<SugVendor> added, ArrayList<SugVendor> suggested, ArrayList<SugVendor> negotiated) {
        addedVendors=added;
        negotiatedVendors=negotiated;
        suggestedVendors=suggested;

    }

    public static ArrayList<SugVendor> getAddedVendors() {
        return  addedVendors;
    }

    public static void addToNegotiated(SugVendor vendor) {
        negotiatedVendors.add(vendor);
    }

    public static void deleteSuggVendor(SugVendor vendor) {
        suggestedVendors.remove(vendor);
    }

    public static void deleteAddedVendor(SugVendor vendor) {
        addedVendors.remove(vendor);
    }

    public static ArrayList<SugVendor> getNegotiatedVendors() {
        return negotiatedVendors;
    }

    public static void deleteNegVendor(SugVendor vendor) {
        negotiatedVendors.remove(vendor);
    }

    public static void addToSuggested(SugVendor vendor) {
        suggestedVendors.add(vendor);
    }

    public static void addVendor(SugVendor vendor) {
        addedVendors.add(vendor);
    }
}
