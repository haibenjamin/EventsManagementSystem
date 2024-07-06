package com.example.client.Util;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Task;
import com.example.client.Model.Upcoming;
import com.example.client.Model.Vendor;
import com.example.client.Model.sugTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class DataManager {
    static ArrayList<Guest> guests;
    static ArrayList<EventData> events;
    static ArrayList<Task> tasks;
    static ArrayList<sugTask> suggestedTasks;
    static ArrayList<SugVendor> suggestedVendors;
    static ArrayList<SugVendor> addedVendors;
    static ArrayList<SugVendor> negotiatedVendors;
    static ArrayList<Upcoming> upcomingList;
    static ArrayList<String> headerList;

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
    public static void deleteUpcomingEvent(int position){
        upcomingList.remove(position);
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
    public static String getUpcomingEventId(int pos){
        return upcomingList.get(pos).getId();
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
    public static ArrayList<sugTask> getSuggestedByType(String type){
        ArrayList<sugTask> newList = new ArrayList<>();
        for (sugTask suggtask : suggestedTasks)
            if (suggtask.getCategory().equals(type))
                newList.add(suggtask);
        return newList;
    }

    public static void setSuggestedTasks(ArrayList<sugTask> suggestedTasks) {
        DataManager.suggestedTasks = suggestedTasks;
    }
    public static ArrayList<String> getSuggestedTasksToString() {
        ArrayList<String> sugTasks= new ArrayList<>();
        int counter=0;
        for (sugTask task : suggestedTasks) {
            sugTasks.add(""+counter);
            for (String taskStr : task.getTasks()) {
                sugTasks.add(taskStr);
            }
        }
        return sugTasks;
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

    public static void setPrice(SugVendor selectedVendor, int price) {
        for (SugVendor vendor : negotiatedVendors){
            if (vendor.equals(selectedVendor))
                vendor.setPriceForService(price);
        }
    }

    public static ArrayList<Upcoming> getUpcomingEvents() {
        return upcomingList;
    }
    public static void setUpcomingList(ArrayList<Upcoming> upcomingList) {
        DataManager.upcomingList = upcomingList;
    }

    public static ArrayList<String> getHeadersList() {
        return headerList;
    }
}
