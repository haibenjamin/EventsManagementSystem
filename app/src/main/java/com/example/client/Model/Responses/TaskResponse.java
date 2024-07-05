package com.example.client.Model.Responses;

import com.example.client.Model.Guest;
import com.example.client.Model.Task;
import com.example.client.Model.sugTask;

import java.util.ArrayList;

public class TaskResponse {
    private ArrayList<Task> cards;
    private ArrayList<Task> tasks;
    private ArrayList<sugTask> suggestedTasks;

    // Getters and Setters
    public ArrayList<Task> getTasks() { return tasks; }
    public void setTasks(ArrayList<Task> tasks) { this.tasks = tasks; }
    public ArrayList<Task> getCards() { return cards; }
    public void setCards(ArrayList<Task> cards) { this.cards = cards; }

    public ArrayList<sugTask> getSuggestedTasks() {
        return suggestedTasks;
    }

    public void setSuggestedTasks(ArrayList<sugTask> suggestedTasks) {
        this.suggestedTasks = suggestedTasks;
    }
}
