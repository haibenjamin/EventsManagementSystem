package com.example.client.Model;

import java.util.ArrayList;

public class sugTask {
    private ArrayList<String> tasks;
    private String category;

    public ArrayList<String> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<String> tasks) {
        this.tasks = tasks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void remove(String selectedTask) {
        tasks.remove(selectedTask);
    }
    @Override
    public String toString() {
        return "sugTask{" +
                "tasks=" + tasks +
                ", category='" + category + '\'' +
                '}';
    }


}
