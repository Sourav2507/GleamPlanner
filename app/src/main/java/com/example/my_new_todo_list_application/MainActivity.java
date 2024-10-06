package com.example.my_new_todo_list_application;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewReminders;
    private ArrayList<Todo> todoList; // Declare todoList here
    private DatabaseHelper2 db; // Declare DatabaseHelper2
    private ArrayAdapter<String> adapter; // Adapter to display strings
    TextView t1, t2;
    FloatingActionButton f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        scheduleMidnightDeletion(this);
        AlarmScheduler.scheduleDailyTaskReminder(this);

        t1 = findViewById(R.id.reminder);
        t1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        });

        t2 = findViewById(R.id.schedule);
        t2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Schedule.class);
            startActivity(intent);
        });

        f1 = findViewById(R.id.fab);
        f1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddTodo.class);
            startActivity(intent);
        });

        listViewReminders = findViewById(R.id.listViewReminders);
        db = new DatabaseHelper2(this);

        // Load and display todos
        loadTodos();

        listViewReminders.setOnItemLongClickListener((parent, view, position, id) -> {
            Todo selectedTodo = todoList.get(position);
            showDeleteConfirmationDialog(selectedTodo.getId(), selectedTodo.getTitle());
            return true;
        });
    }

    private void loadTodos() {
        todoList = new ArrayList<>(db.getAllTodos()); // Initialize todoList from db
        List<String> titles = new ArrayList<>();

        // Check if todoList is empty
        if (todoList.isEmpty()) {
            titles.add("No tasks available."); // Add a placeholder
        } else {
            for (Todo todo : todoList) {
                titles.add(todo.getTitle() + " - " + todo.getTime() + " (" + todo.getCategory() + ")");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles);
        listViewReminders.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog(int todoId, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task")
                .setMessage("Are you sure you want to delete the task: " + title + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteTodo(todoId);
                    Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                    loadTodos(); // Refresh the list after deletion
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void scheduleMidnightDeletion(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Set to 0 for midnight
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Check if the time is before the current time; if so, schedule for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        Intent intent = new Intent(context, MidnightReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

}
