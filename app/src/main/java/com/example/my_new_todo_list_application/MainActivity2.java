package com.example.my_new_todo_list_application;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List; // Changed from ArrayList to List

public class MainActivity2 extends AppCompatActivity {

    TextView t1,t2;
    private ListView listView;
    private Button deleteAllButton; // Ensure this button exists in your XML layout
    private FloatingActionButton fab;
    private DatabaseHelper db; // Your database helper class
    private List<Reminder> reminders; // Changed to List<Reminder>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        t1=findViewById(R.id.todo);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
            }
        });
        t2 = findViewById(R.id.schedule);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this,Schedule.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.listViewReminders); // ListView to display reminders
        deleteAllButton = findViewById(R.id.deleteAllButton); // Ensure this ID matches your XML
        fab = findViewById(R.id.floatingActionButton); // Floating Action Button
        db = new DatabaseHelper(this); // Initialize the database helper

        loadReminders();

        // FAB click listener to add a new reminder
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity2.this, AddReminder.class);
            startActivity(intent);
        });

        deleteAllButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete All Reminders")
                    .setMessage("Are you sure you want to delete all reminders?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteAllReminders(); // Method to delete all reminders
                        loadReminders(); // Refresh the list
                        Toast.makeText(MainActivity2.this, "All reminders deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Long click listener for deleting individual reminders
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Reminder reminderToDelete = reminders.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Delete Reminder")
                    .setMessage("Are you sure you want to delete this reminder?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.deleteReminder(reminderToDelete.getId()); // Delete from the database
                        loadReminders(); // Refresh the list
                        Toast.makeText(MainActivity2.this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true; // Indicate that the long click was handled
        });
    }

    private void loadReminders() {
        reminders = db.getAllReminders(); // Fetch all reminders from the database
        ReminderAdapter adapter = new ReminderAdapter(this, new ArrayList<>(reminders)); // Use custom adapter
        listView.setAdapter(adapter); // Set the adapter for the ListView
    }
}
