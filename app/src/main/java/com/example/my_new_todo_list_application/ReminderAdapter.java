package com.example.my_new_todo_list_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        super(context, 0, reminders); // Call to parent constructor
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Reminder reminder = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_list_item, parent, false);
        }

        // Lookup views for data population
        TextView titleTextView = convertView.findViewById(R.id.reminderTitle);
        TextView dateTextView = convertView.findViewById(R.id.reminderDate);
        TextView timeTextView = convertView.findViewById(R.id.reminderTime);

        // Populate the data into the template view using the reminder object
        titleTextView.setText(reminder.getTitle());
        dateTextView.setText("Date: " + reminder.getDate()); // Assume getDate() returns a formatted date string
        timeTextView.setText("Time: " + reminder.getTime()); // Assume getTime() returns a formatted time string

        // Return the completed view to render on screen
        return convertView;
    }
}
