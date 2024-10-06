package com.example.my_new_todo_list_application;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
                // Set the alarm for the selected time
                setAlarmForSchedule(selectedTime);

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

    private void setAlarmForSchedule(String selectedTime) {
        // Split the selectedTime to get hour and minute
        String[] timeParts = selectedTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Create a Calendar object
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour); // set the hour
        calendar.set(Calendar.MINUTE, minute); // set the minute
        calendar.set(Calendar.SECOND, 0);

        // If the selected time is in the past, move it to the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Create the alarm intent
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("time", selectedTime); // Pass any extra data if needed
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
