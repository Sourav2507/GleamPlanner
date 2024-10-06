package com.example.my_new_todo_list_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MidnightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper2 db = new DatabaseHelper2(context);
        db.deleteAllTodos(); // Delete all tasks from the database
        Log.d("MidnightReceiver", "All tasks deleted at midnight.");
    }
}
