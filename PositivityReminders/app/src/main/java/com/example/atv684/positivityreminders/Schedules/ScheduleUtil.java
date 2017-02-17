package com.example.atv684.positivityreminders.schedules;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.atv684.positivityreminders.NotificationBroadcastReceiver;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.text.SimpleDateFormat;

public class ScheduleUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");

    public static void deleteSchedule(Context context, ScheduleObject object) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) object.id, notificationIntent, 0);
        alarmManager.cancel(pendingIntent);

        QuoteDBHelper.get(context).deleteSchedule(object);

        Log.e("deleted", "deleted schedule");
    }
}
