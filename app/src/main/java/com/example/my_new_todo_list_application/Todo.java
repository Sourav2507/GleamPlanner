package com.example.my_new_todo_list_application;

public class Todo {
    private int id;
    private String title;
    private String time;
    private String category;

    public Todo() {}

    public Todo(String title, String time, String category) {
        this.title = title;
        this.time = time;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
