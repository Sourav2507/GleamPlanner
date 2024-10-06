package com.example.my_new_todo_list_application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.TimePickerDialog;

import java.util.Calendar;

public class AddTodo extends AppCompatActivity {
    private EditText titleEditText;
    private TextView timeTextView;
    private Spinner categorySpinner;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        titleEditText = findViewById(R.id.title);
        timeTextView = findViewById(R.id.selecttime);
        categorySpinner = findViewById(R.id.category_spinner);
        TextView addTaskButton = findViewById(R.id.setreminder);
        Button timeButton = findViewById(R.id.time_button);

        // Set up the Category Spinner
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set up the time picker dialog
        timeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodo.this,
                    (view, selectedHour, selectedMinute) -> {
                        selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        timeTextView.setText(selectedTime);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // Add Task Button OnClick
        addTaskButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString().trim();
            String category = categorySpinner.getSelectedItem().toString();

            if (title.isEmpty() || selectedTime == null || category.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseHelper2 db = new DatabaseHelper2(this);
            Todo todo = new Todo(title, selectedTime, category);
            long result = db.addTodo(todo);

            if (result != -1) {
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTodo.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
