package com.example.atv684.positivityreminders.Schedules;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.atv684.positivityreminders.NotificationBroadcastReceiver;
import com.example.atv684.positivityreminders.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by atv684 on 10/12/16.
 */
public class NotificationScheduler {

    Context context;

    public NotificationScheduler(Context context){
        this.context = context;
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText("TEST");
        builder.setSmallIcon(R.drawable.ic_chat_white_24dp);
        return builder.build();
    }

    public void scheduleNotification(){

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long diff = getNextSchedule().getStartTime().getTime() - new Date().getTime();
        long futureInMillis = diff;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public ScheduleObject getNextSchedule(){

        ArrayList<ScheduleObject> schedules = ScheduleUtil.getSchedules(context);

        Date startTime = null;
        Date endTime;

        ScheduleObject returnSchedule = null;

        Date now = new Date();

        for(ScheduleObject schedule : schedules){

            long diff = schedule.getStartTime().getTime() - now.getTime();
            long startDiff = -1;

            if(startTime != null){
                startDiff = startTime.getTime() - now.getTime();
            }
            if(startTime == null || diff < startDiff && diff > 0){
                startTime = schedule.getStartTime();
                endTime = schedule.getEndTime();
                returnSchedule = schedule;
            }

        }

        return returnSchedule;
    }

}
