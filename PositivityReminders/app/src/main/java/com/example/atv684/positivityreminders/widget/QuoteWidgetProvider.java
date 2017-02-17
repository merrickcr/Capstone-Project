package com.example.atv684.positivityreminders.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.widget.RemoteViews;

import com.example.atv684.positivityreminders.GetImageFromDBAsyncTask;
import com.example.atv684.positivityreminders.QuoteObject;
import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.ArrayList;
import java.util.Random;

public class QuoteWidgetProvider extends AppWidgetProvider implements QuoteDBHelper.DBHelperCallbackListener {

    Context context;

    AppWidgetManager appWidgetManager;

    int[] appWidgetIds;

    private QuoteObject quote;

    private ArrayList<QuoteObject> quotes;

    /**
     * http://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color
     * as seen on stack overflow
     *
     * @param colorIntValue
     * @return
     */
    // Put this method in whichever class you deem appropriate
    // static or non-static, up to you.
    public static int getContrastColor(int colorIntValue) {
        int red = Color.red(colorIntValue);
        int green = Color.green(colorIntValue);
        int blue = Color.blue(colorIntValue);
        double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
        return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, int[] ids) {
        final int count = ids.length;

        appWidgetIds = ids;

        QuoteDBHelper dbHelper = QuoteDBHelper.get(context, this);

        Cursor c = dbHelper.fetchQuotesCursor(null, null, null, null);

        this.context = context;
        this.appWidgetManager = appWidgetManager;

        quotes = new ArrayList<>();

        while (c.moveToNext()) {
            quotes.add(new QuoteObject(c));
        }

        new GetImageFromDBAsyncTask(context) {
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                Palette palette = Palette.from(bitmap).generate();

                final Context finalContext = context;
                final AppWidgetManager manager = appWidgetManager;

                int color = palette.getDominantColor(ContextCompat.getColor(finalContext, R.color.white));

                int contrastColor = getContrastColor(color);

                int count = appWidgetIds.length;
                for (int i = 0; i < count; i++) {
                    int widgetId = appWidgetIds[i];

                    quote = quotes.get(new Random().nextInt(quotes.size()));

                    RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

                    remoteViews.setTextViewText(R.id.text, quote.getText());
                    remoteViews.setTextColor(R.id.text, contrastColor);
                    remoteViews.setTextViewText(R.id.author, quote.getAuthor());
                    remoteViews.setTextColor(R.id.author, contrastColor);

                    remoteViews.setImageViewBitmap(R.id.image, bitmap);

                    Intent intent = new Intent(context, QuoteWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.card_content, pendingIntent);

                    manager.updateAppWidget(widgetId, remoteViews);
                }
            }
        }.execute("");
    }

    @Override
    public void onLoadOnlineQuotes() {

    }

    @Override
    public void onDataFinished(ArrayList<QuoteObject> quotes) {

    }

}
