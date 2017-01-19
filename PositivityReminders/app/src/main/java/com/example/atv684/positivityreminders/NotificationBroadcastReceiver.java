package com.example.atv684.positivityreminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.atv684.positivityreminders.Schedules.NotificationScheduler;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;


public class NotificationBroadcastReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    private void showNotification(Context context) {

        QuoteDBHelper dbHelper = new QuoteDBHelper(context, null);

        QuoteObject quote = dbHelper.readQuote();

        Log.i("notification", "visible");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
            new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.notification_title))
                .setSmallIcon(R.drawable.ic_chat_white_24dp)
                .setContentText(quote.getText() != null ? quote.getText() : "inspirational quote")
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(quote.getText()));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }

}
