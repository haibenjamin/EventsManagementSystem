package com.example.client.Model;

public class Task {
    String title;
    String _id;
    String column;
    String event;
    boolean selected;

    public Task(String title,String column){
        this.title=title;
        this.column=column;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    long createAt;
    long updatedAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelected(){
        this.selected=!selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    @Override
    public String toString() {
        return "Task{title='" + title + "', column='" + column +"', id='" + _id + "'}";
    }


}
