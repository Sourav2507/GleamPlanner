package com.example.my_new_todo_list_application;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

public class NotificationHelper {
    private Context context;
    private DatabaseHelper2 db;

    public NotificationHelper(Context context) {
        this.context = context;
        db = new DatabaseHelper2(context); // Initialize your DatabaseHelper
    }

    public void sendPendingTasksNotification() {
        ArrayList<Todo> pendingTasks = (ArrayList<Todo>) db.getAllTodos(); // Fetch tasks from database

        if (pendingTasks.isEmpty()) {
            return; // No pending tasks, do nothing
        }

        StringBuilder messageBuilder = new StringBuilder("You have pending tasks:\n");
        for (Todo todo : pendingTasks) {
            messageBuilder.append("- ").append(todo.getTitle()).append("\n");
        }

        sendNotification("Pending Tasks Reminder", messageBuilder.toString());
    }

    private void sendNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "task_reminder_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Task Reminders", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, MainActivity.class); // Redirect to MainActivity on click
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_24) // Set your small custom icon here
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        notificationManager.notify(2, builder.build());
    }

}
