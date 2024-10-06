package com.example.my_new_todo_list_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper1 extends SQLiteOpenHelper {

    // Database Information
    private static final String DATABASE_NAME = "schedules.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    private static final String TABLE_SCHEDULES = "schedules";

    // Column Names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_CATEGORY = "category";

    // Create Table SQL Query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_SCHEDULES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_PRIORITY + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT);";

    public DatabaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Create the table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULES);
        // Create tables again
        onCreate(db);
    }

    // Method to add a schedule
    public long addSchedule(Schedules schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, schedule.getTime());
        values.put(COLUMN_PRIORITY, schedule.getPriority());
        values.put(COLUMN_CATEGORY, schedule.getCategory());

        // Inserting Row and returning the ID of the new row
        long result = db.insert(TABLE_SCHEDULES, null, values);
        db.close(); // Closing database connection
        return result;
    }

    // Method to get all schedules
    public ArrayList<Schedules> getAllSchedules() {
        ArrayList<Schedules> scheduleList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCHEDULES, null, null, null, null, null, null);

        // Loop through the cursor and add schedules to the list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the indices for the columns safely
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                int timeIndex = cursor.getColumnIndexOrThrow(COLUMN_TIME);
                int priorityIndex = cursor.getColumnIndexOrThrow(COLUMN_PRIORITY);
                int categoryIndex = cursor.getColumnIndexOrThrow(COLUMN_CATEGORY);

                // Retrieve values using the indices
                int id = cursor.getInt(idIndex); // Get the ID
                String time = cursor.getString(timeIndex);
                String priority = cursor.getString(priorityIndex);
                String category = cursor.getString(categoryIndex);

                scheduleList.add(new Schedules(id, time, priority, category)); // Include ID
            } while (cursor.moveToNext());
            cursor.close(); // Close cursor after use
        }

        db.close(); // Closing database connection
        return scheduleList; // Return the list of schedules
    }



    // Method to delete a schedule
    public void deleteSchedule(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close(); // Closing database connection
    }
}
