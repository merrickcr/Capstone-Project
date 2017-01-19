package com.example.atv684.positivityreminders.Schedules;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.atv684.positivityreminders.NotificationBroadcastReceiver;
import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public void scheduleNotification(ScheduleObject scheduleObject){

        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ((int)scheduleObject.id), notificationIntent,
            PendingIntent
            .FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, scheduleObject.getStartTime().getTime(), AlarmManager.INTERVAL_FIFTEEN_MINUTES ,
            pendingIntent);

    }

    public ScheduleObject getNextSchedule(){

        ArrayList<ScheduleObject> schedules = new QuoteDBHelper(context, null).getSchedules();

        Date startTime = null;
        Date endTime;

        ScheduleObject returnSchedule = null;

        Date now = new Date();
        long diff = 0;
        long currentDif = Long.MAX_VALUE;

        for(ScheduleObject schedule : schedules){
            if(schedule.startTime != null){
                diff = schedule.startTime.getTime() - now.getTime();
            }
            if(startTime == null || diff < currentDif && diff > 0){
                startTime = schedule.getStartTime();
                returnSchedule = schedule;
                currentDif = diff;
            }

        }
        return returnSchedule;
    }

    public int compareTimes(Date d1, Date d2)
    {
        int     t1;
        int     t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }

}
