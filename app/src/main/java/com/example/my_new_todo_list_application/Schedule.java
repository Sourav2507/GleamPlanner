package com.example.my_new_todo_list_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Schedule extends AppCompatActivity {

    TextView t1, t2;
    FloatingActionButton fab;
    private ListView scheduleListView;
    private ScheduleAdapter scheduleAdapter;
    private ArrayList<Schedules> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule);

        t1 = findViewById(R.id.todo);
        t1.setOnClickListener(view -> {
            Intent intent = new Intent(Schedule.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        t2 = findViewById(R.id.reminder);
        t2.setOnClickListener(view -> {
            Intent intent = new Intent(Schedule.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Schedule.this, AddSchedule.class);
            startActivity(intent);
            finish();
        });

        scheduleListView = findViewById(R.id.listViewReminders);
        schedules = new ArrayList<>();

        // Fetch schedules from the database
        DatabaseHelper1 db = new DatabaseHelper1(this);
        schedules.addAll(db.getAllSchedules()); // Assuming you have this method in your DatabaseHelper

        // Check if schedules are empty
        if (schedules.isEmpty()) {
            Toast.makeText(this, "No schedules found", Toast.LENGTH_SHORT).show();
        } else {
            // Set up the adapter and bind data
            scheduleAdapter = new ScheduleAdapter(this, schedules);
            scheduleListView.setAdapter(scheduleAdapter);
        }
    }

}
