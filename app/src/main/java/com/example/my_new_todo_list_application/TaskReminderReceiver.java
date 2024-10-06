package com.example.my_new_todo_list_application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TaskReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Fetch tasks from the database and send notification
        new NotificationHelper(context).sendPendingTasksNotification();
    }
}
