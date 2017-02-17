package com.example.atv684.positivityreminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.example.atv684.positivityreminders.provider.QuotesContract;
import com.example.atv684.positivityreminders.schedules.NotificationScheduler;
import com.example.atv684.positivityreminders.schedules.ScheduleObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NotificationBroadcastReceiver extends BroadcastReceiver {

    Context context;

    private ScheduleObject schedule;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //reboot alarm
            //get all the schedules
            ArrayList<ScheduleObject> schedules = QuoteDBHelper.get(context).getSchedules();

            NotificationScheduler scheduler = new NotificationScheduler(context);
            for (ScheduleObject object : schedules) {
                scheduler.scheduleNotification(object);
            }
        } else {

            long id = intent.getLongExtra("schedule", -1);

            showNotification(context, id);
        }
    }

    private void showNotification(Context context, long id) {

        QuoteDBHelper dbHelper = QuoteDBHelper.get(context);

        QuoteObject quote = dbHelper.readQuote();

        Date today = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("EEE");

        schedule = dbHelper.getSchedule(id);

        String day = "";

        switch (formatter.format(today)) {
            case "Mon":
                day = QuotesContract.ScheduleEntry.MONDAY;
                break;
            case "Tue":
                day = QuotesContract.ScheduleEntry.TUESDAY;
                break;
            case "Wed":
                day = QuotesContract.ScheduleEntry.WEDNESDAY;
                break;
            case "Thu":
                day = QuotesContract.ScheduleEntry.THURSDAY;
                break;
            case "Fri":
                day = QuotesContract.ScheduleEntry.FRIDAY;
                break;
            case "Sat":
                day = QuotesContract.ScheduleEntry.SATURDAY;
                break;
            case "Sun":
                day = QuotesContract.ScheduleEntry.SUNDAY;
                break;
        }

        if (schedule.getDays() == null || schedule.getDays().isEmpty() || schedule.getDays().contains(day) == false) {
            return;
        }

        Log.i("notification", "visible");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
            new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_chat_white_24dp)
                .setContentText(quote.getText() != null ? quote.getText() : "inspirational quote")
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(quote.getText() != null ? quote.getText() : "inspirational quote"));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

}
