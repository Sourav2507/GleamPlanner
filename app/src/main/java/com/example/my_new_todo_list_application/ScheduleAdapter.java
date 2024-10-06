package com.example.my_new_todo_list_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<Schedules> {
    private final DatabaseHelper1 db; // Database helper

    public ScheduleAdapter(Context context, ArrayList<Schedules> schedules) {
        super(context, 0, schedules);
        db = new DatabaseHelper1(context); // Initialize database helper
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Schedules schedule = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        // Lookup view for data population
        TextView timeTextView = convertView.findViewById(android.R.id.text1);
        TextView detailsTextView = convertView.findViewById(android.R.id.text2);

        // Populate the data into the template view
        timeTextView.setText(schedule.getTime());
        detailsTextView.setText(schedule.getPriority() + " - " + schedule.getCategory());

        // Set a long-click listener to delete the item
        convertView.setOnLongClickListener(v -> {
            showDeleteConfirmationDialog(schedule.getId(), position); // Show confirmation dialog
            return true; // Indicate that the long-click was handled
        });

        // Return the completed view to render on screen
        return convertView;
    }

    // Method to show confirmation dialog before deletion
    private void showDeleteConfirmationDialog(int scheduleId, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Schedule")
                .setMessage("Are you sure you want to delete this schedule?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteSchedule(scheduleId); // Delete the schedule from the database
                    remove(getItem(position)); // Remove the item from the adapter
                    notifyDataSetChanged(); // Notify the adapter to refresh the list
                    Toast.makeText(getContext(), "Schedule deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
