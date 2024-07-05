package com.example.client.Model;

import java.util.ArrayList;

public class TasksArray {
    private ArrayList<Task> cards;

    public ArrayList<Task> getTasks(){
        return cards;
    }
    public void setTasks(ArrayList<Task> tasks){
        this.cards=tasks;
    }

    public String toString(){
        String str="tasks[";
        for(Task task : cards){
            str+=task.toString()+" ";

        }
        return str+"]";
    }
}
