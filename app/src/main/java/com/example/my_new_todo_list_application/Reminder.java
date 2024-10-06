package com.example.my_new_todo_list_application;

public class Reminder {
    private int id; // Assuming you have an id field
    private String title;
    private String date;
    private String time;
    private String priority;
    private String category;

    // Default constructor (if you need it)
    public Reminder() {}

    // Constructor with parameters
    public Reminder(String title, String date, String time, String priority, String category) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.category = category;
    }

    // Getters and Setters
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Override toString if necessary for display in ListView
    @Override
    public String toString() {
        return title; // Customize as needed for displaying reminders
    }
}
