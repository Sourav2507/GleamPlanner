package com.example.my_new_todo_list_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;

import java.util.Calendar;

public class AddSchedule extends AppCompatActivity {
    private TextView scheduleTimeTextView;
    private Spinner prioritySpinner;
    private Spinner categorySpinner;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleTimeTextView = findViewById(R.id.selecttime);
        prioritySpinner = findViewById(R.id.priority_spinner);
        categorySpinner = findViewById(R.id.category_spinner);
        TextView saveScheduleButton = findViewById(R.id.setreminder);
        Button timeButton = findViewById(R.id.time_button); // Reference to your time button

        // Set up the Priority Spinner
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // Set up the Category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set up the time picker dialog
        timeButton.setOnClickListener(v -> {
            // Get current time
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddSchedule.this,
                    (view, selectedHour, selectedMinute) -> {
                        // Format time
                        selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        scheduleTimeTextView.setText(selectedTime); // Set the selected time in TextView
                    }, hour, minute, true);
            timePickerDialog.show(); // Show the TimePickerDialog
        });

        // Save Schedule Button OnClick
        saveScheduleButton.setOnClickListener(v -> {
            String priority = prioritySpinner.getSelectedItem().toString();
            String category = categorySpinner.getSelectedItem().toString();

            if (selectedTime == null || priority.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save the schedule to the database
            DatabaseHelper1 db = new DatabaseHelper1(this);
            Schedules schedule = new Schedules(0, selectedTime, priority, category);  // ID will be auto-generated

            long result = db.addSchedule(schedule); // Assuming you have this method implemented in your DatabaseHelper

            if (result != -1) {
                Toast.makeText(this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                // Redirect to the Schedule activity
                Intent intent = new Intent(AddSchedule.this, Schedule.class);
                startActivity(intent);
                finish(); // Finish this activity to remove it from the back stack
            } else {
                Toast.makeText(this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
