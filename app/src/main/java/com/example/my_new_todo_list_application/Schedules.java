package com.example.my_new_todo_list_application;

public class Schedules {
    private String time;
    private String priority;
    private String category;
    private int id; // Add ID attribute

    public Schedules(int id, String time, String priority, String category) {
        this.id = id; // Initialize ID
        this.time = time;
        this.priority = priority;
        this.category = category;
    }

    // Getters
    public String getTime() { return time; }
    public String getPriority() { return priority; }
    public String getCategory() { return category; }
    public int getId() { return id; } // Getter for ID
}
