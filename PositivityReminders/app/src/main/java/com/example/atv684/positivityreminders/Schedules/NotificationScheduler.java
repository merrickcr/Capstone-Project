package com.example.atv684.positivityreminders.schedules;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.atv684.positivityreminders.NotificationBroadcastReceiver;
import com.example.atv684.positivityreminders.R;

public class NotificationScheduler {

    Context context;

    public NotificationScheduler(Context context) {
        this.context = context;
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText("TEST");
        builder.setSmallIcon(R.drawable.ic_chat_white_24dp);
        return builder.build();
    }

    public void scheduleNotification(ScheduleObject scheduleObject) {

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);

        notificationIntent.putExtra("schedule", scheduleObject.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ((int) scheduleObject.id), notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, scheduleObject.getStartTime().getTime(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

}
