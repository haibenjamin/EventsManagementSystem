package com.example.client.Model;

public class NewTask {

    private String title;
    private String column;

    public NewTask(String title, String column) {
        this.title = title;
        this.column = column;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "NewCard{" +
                "title='" + title + '\'' +
                ", column='" + column + '\'' +
                '}';
    }
}
