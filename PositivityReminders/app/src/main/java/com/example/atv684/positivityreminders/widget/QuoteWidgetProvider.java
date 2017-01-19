package com.example.atv684.positivityreminders.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.atv684.positivityreminders.QuoteObject;
import com.example.atv684.positivityreminders.R;
import com.example.atv684.positivityreminders.provider.QuoteDBHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class QuoteWidgetProvider extends AppWidgetProvider implements QuoteDBHelper.DBHelperCallbackListener {

    Context context;

    AppWidgetManager appWidgetManager;

    int[] appWidgetIds;

    private QuoteObject quote;

    private ArrayList<QuoteObject> quotes;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        QuoteDBHelper dbHelper = new QuoteDBHelper(context, this);

        Cursor c = dbHelper.fetchQuotesCursor(null, null, null, null);

        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        quotes = new ArrayList<>();

        while (c.moveToNext()) {
            quotes.add(new QuoteObject(c));
        }

        Picasso.with(context)
            .load("https://source.unsplash.com/category/nature/800x600")
            .into(target);
    }

    Target target = new Target() {


        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Palette palette = Palette.from(bitmap).generate();

            int color = palette.getDarkVibrantColor(ContextCompat.getColor(context, R.color.white));

            int count = appWidgetIds.length;
            for (int i = 0; i < count; i++) {
                int widgetId = appWidgetIds[i];

                quote = quotes.get(new Random().nextInt(quotes.size()));

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

                remoteViews.setTextViewText(R.id.text, quote.getText());
                remoteViews.setTextColor(R.id.text, color);
                remoteViews.setTextViewText(R.id.author, quote.getAuthor());
                remoteViews.setTextColor(R.id.author, color);

                remoteViews.setImageViewBitmap(R.id.image, bitmap);

                Intent intent = new Intent(context, QuoteWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                remoteViews.setOnClickPendingIntent(R.id.card_content, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

            Log.e("asdf", "asdf");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.e("asdf", "asdf");
        }
    };


    @Override
    public void onLoadOnlineQuotes() {

    }

    @Override
    public void onDataFinished(ArrayList<QuoteObject> quotes) {

    }
}
