package com.example.my_new_todo_list_application;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddReminder extends AppCompatActivity {

    private EditText titleEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private Spinner prioritySpinner;
    private Spinner categorySpinner;
    private String selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        titleEditText = findViewById(R.id.title);
        dateTextView = findViewById(R.id.selectdate);
        timeTextView = findViewById(R.id.selecttime);
        prioritySpinner = findViewById(R.id.priority_spinner);
        categorySpinner = findViewById(R.id.category_spinner);

        Button dateButton = findViewById(R.id.date_button);
        Button timeButton = findViewById(R.id.time_button);
        TextView setReminderTextView = findViewById(R.id.setreminder);

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

        // Date Picker
        dateButton.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminder.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear; // Date format
                        dateTextView.setText(selectedDate); // Update TextView
                    }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker
        timeButton.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminder.this,
                    (view, selectedHour, selectedMinute) -> {
                        selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute); // Time format
                        timeTextView.setText(selectedTime); // Update TextView
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // Set Reminder
        setReminderTextView.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String priority = prioritySpinner.getSelectedItem().toString();
            String category = categorySpinner.getSelectedItem().toString();

            if (title.isEmpty() || selectedDate == null || selectedTime == null) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save to database (assuming you have a method for this)
            DatabaseHelper db = new DatabaseHelper(this);
            db.addReminder(new Reminder(title, selectedDate, selectedTime, priority, category));

            // Redirect to MainActivity2
            Intent intent = new Intent(AddReminder.this, MainActivity2.class);
            startActivity(intent);
            finish(); // Finish this activity to remove it from the back stack
        });
    }
}
