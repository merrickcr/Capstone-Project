package com.example.atv684.positivityreminders.provider;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.atv684.positivityreminders.BaseActivity;
import com.example.atv684.positivityreminders.ImageUtil;
import com.example.atv684.positivityreminders.QuoteObject;
import com.example.atv684.positivityreminders.schedules.ScheduleObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created Chris on 9/19/16.
 */
public class QuoteDBHelper extends SQLiteOpenHelper implements LoaderManager.LoaderCallbacks<Cursor> {

    final static String DATABASE_NAME = "quote.db";

    final static int DATABASE_VERSION = 3;

    private static final int IMAGE_REQUEST_MAX = 25;

    private static final Object LOCK_OBJECT = new Object();

    private static final int REQUEST_COUNT_MAX = 20;

    // Table Names
    private static final String TABLE_NAME = "images";

    // column names
    private static final String IMAGE_KEY_NAME = "image_name";

    private static final String IMAGE_KEY_IMAGE = "image_data";

    // Table create statement
    private static final String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
        "_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
        IMAGE_KEY_NAME + " TEXT," +
        IMAGE_KEY_IMAGE + " BLOB UNIQUE);";

    private static final String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + QuotesContract.ScheduleEntry.TABLE_NAME + "(" +
        QuotesContract.ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        QuotesContract.ScheduleEntry.COLUMN_TIME + " STRING," +
        QuotesContract.ScheduleEntry.COLUMN_DAYS + " STRING);";

    private static volatile QuoteDBHelper instance;

    private Context context;

    private int requestCount = 0;

    private int imageRequestCount = 0;

    private SQLiteDatabase db;

    private DBHelperCallbackListener listener;

    private ArrayList<Request> requestQueue = new ArrayList<Request>();

    private QuoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @NonNull
    public static QuoteDBHelper get(@NonNull Context context) {
        if (instance == null) {
            synchronized (QuoteDBHelper.class) {
                instance = new QuoteDBHelper(context);
            }
        } else {
            instance.context = context;
        }
        return instance;
    }

    @NonNull
    public static QuoteDBHelper get(@NonNull Context context, @NonNull DBHelperCallbackListener listener) {
        instance = get(context);
        instance.setListener(listener);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;
        setupQuoteDB(db);
    }

    private void setupQuoteDB(SQLiteDatabase db) {
        final String SQL_CREATE_QUOTE_TABLE = "CREATE TABLE " + QuotesContract.QuoteEntry.TABLE_NAME + " (" +
            QuotesContract.QuoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

            QuotesContract.QuoteEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
            QuotesContract.QuoteEntry.COLUMN_TEXT + " TEXT NOT NULL, " +
            QuotesContract.QuoteEntry.COLUMN_CUSTOM + " BOOLEAN, " +
            QuotesContract.QuoteEntry.COLUMN_FAVORITE + " BOOLEAN, " +
            QuotesContract.QuoteEntry.COLUMN_NUM_VIEWS + " INTEGER, " +
            QuotesContract.QuoteEntry.COLUMN_IMAGE + " STRING," +
            "UNIQUE (" + QuotesContract.QuoteEntry.COLUMN_TEXT + ")" +
            ")";

        Log.e("DBHElper", SQL_CREATE_QUOTE_TABLE);

        db.execSQL(SQL_CREATE_QUOTE_TABLE);

        db.execSQL(CREATE_IMAGE_TABLE);

        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public long addImage(Bitmap bitmap) {

        Bitmap
            scaled =
            ImageUtil.decodeSampledBitmapFromResource(ImageUtil.getBitmapAsByteArray(bitmap), bitmap.getHeight(), bitmap.getWidth
                ());

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(IMAGE_KEY_NAME, "image" + new Random(SystemClock.currentThreadTimeMillis()).nextInt(100000));
        values.put(IMAGE_KEY_IMAGE, ImageUtil.getBitmapAsByteArray(scaled));

        return db.insert(TABLE_NAME, null, values);
    }

    public byte[] getImage() {

        db = getWritableDatabase();

        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (c.getCount() <= 0 || !c.move(new Random().nextInt(c.getCount()))) {
            return null;
        }

        byte[] blob = c.getBlob(c.getColumnIndex(IMAGE_KEY_IMAGE));

        //c.close();

        return blob;
    }

    public long addSchedule(ScheduleObject schedule) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(QuotesContract.ScheduleEntry.COLUMN_DAYS, (schedule.getDaysJSONArray() != null) ? schedule.getDaysJSONArray()
            .toString() : "");
        values.put(QuotesContract.ScheduleEntry.COLUMN_TIME, (schedule.getStartTime() != null) ? schedule.getStartTime().toString() : "");

        long result = db.insert(QuotesContract.ScheduleEntry.TABLE_NAME, null, values);

        Log.e("result", "result = " + result);

        return result;
    }

    public void deleteSchedule(ScheduleObject schedule) {

        db = getWritableDatabase();

        int result = db.delete(QuotesContract.ScheduleEntry.TABLE_NAME, "_id = ?", new String[]{String.valueOf(schedule.getId())});

        Log.e("deleting", "deleted results = " + result);

    }

    public ArrayList<ScheduleObject> getSchedules() {
        db = getReadableDatabase();

        Cursor c = db.query(QuotesContract.ScheduleEntry.TABLE_NAME, null, null, null, null, null, null);

        ArrayList<ScheduleObject> schedules = new ArrayList<ScheduleObject>();

        while (c.moveToNext()) {
            schedules.add(new ScheduleObject(c));
        }

        c.close();

        return schedules;
    }


    public void addQuote(String text, String author, String imageUri) {

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, author);
        values.put(QuotesContract.QuoteEntry.COLUMN_TEXT, text);
        values.put(QuotesContract.QuoteEntry.COLUMN_IMAGE, imageUri);
        values.put(QuotesContract.QuoteEntry.COLUMN_CUSTOM, 1);

        try {
            db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);
        } catch (Exception e) {

        }
    }

    public void addQuote(QuoteObject quote) {

        SQLiteDatabase db = getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(QuotesContract.QuoteEntry.COLUMN_TEXT, quote.getText());

        Cursor result = db.query(QuotesContract.QuoteEntry.TABLE_NAME, null, QuotesContract.QuoteEntry.COLUMN_TEXT + " = ?", new
            String[]{quote.getText()}, null, null, null);

        if (result.moveToFirst() == true) {
            return;
        }

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, quote.getAuthor());
        values.put(QuotesContract.QuoteEntry.COLUMN_TEXT, quote.getText());

        try {
            db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }

    }

    public void deleteQuoteTable() {

        db.execSQL("DROP TABLE " + QuotesContract.QuoteEntry.TABLE_NAME);

    }


    public void updateQuote(QuoteObject object) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, object.getAuthor());
        values.put(QuotesContract.QuoteEntry.COLUMN_TEXT, object.getText());
        values.put(QuotesContract.QuoteEntry.COLUMN_IMAGE, object.getImageURI());
        values.put(QuotesContract.QuoteEntry.COLUMN_FAVORITE, object.isFavorite());
        values.put(QuotesContract.QuoteEntry.COLUMN_CUSTOM, object.isCustom());
        values.put(QuotesContract.QuoteEntry.COLUMN_NUM_VIEWS, object.getNumViews());

        int result = db.update(QuotesContract.QuoteEntry.TABLE_NAME, values, QuotesContract.QuoteEntry._ID + " = " + object.getId(), null);

        Log.e("AF", result + "");


    }

    public int updateQuote(ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = getWritableDatabase();

        return db.update(QuotesContract.QuoteEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public QuoteObject readQuote() {
        QuoteObject object = null;

        SQLiteDatabase db = getReadableDatabase();

        Random r = new Random();

        Cursor c = db.query(QuotesContract.QuoteEntry.TABLE_NAME, null, null, null, null, null, null);

        if (c.getCount() <= 0) {
            return null;
        }

        int index = r.nextInt(c.getCount());

        c.moveToPosition(index);

        object = new QuoteObject(c);

        return object;

    }

    public Cursor fetchQuotesCursor(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(QuotesContract.QuoteEntry.TABLE_NAME);

        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return c;
    }


    public void fetchQuotesFromOnline() {

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        });

        OkHttpClient client = new OkHttpClient();

        try {

            Request request = new Request.Builder()
                .url("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en")
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //do nothing?
                    Log.e("DB REQUEST", "FAILED");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final Call finalCall = call;
                    final Response finalResponse = response;

                    QuoteObject quote = new QuoteObject(finalResponse.body().string());

                    //String urlImage = getImageForQuote();

                    addQuote(quote);

                    if (requestCount >= REQUEST_COUNT_MAX) {

                        if (listener != null) {
                            listener.onLoadOnlineQuotes();
                            requestCount = 0;
                        }
                    } else {
                        requestCount++;
                        fetchQuotesFromOnline();
                    }
                }
            });

        } catch (Exception e) {
            Log.e("DBHelper", e.getMessage());
        }


    }


    public long insertQuote(ContentValues values) {

        SQLiteDatabase db = getWritableDatabase();

        long result = db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);

        //db.close();

        return result;
    }

    public int deleteQuote(String selection, String[] selectionArgs) {

        db = getWritableDatabase();

        int result = db.delete(QuotesContract.QuoteEntry.TABLE_NAME, selection, selectionArgs);

        db.close();

        return result;

    }

    public Cursor fetchFavoriteQuotes() {

        Cursor c = context.getContentResolver().query(QuoteProvider.CONTENT_URI, null, QuotesContract.QuoteEntry
            .COLUMN_FAVORITE + " = ?", new String[]{"1"}, null);

        return c;
    }

    public Cursor fetchCustomQuotes() {

        Cursor c = context.getContentResolver().query(QuoteProvider.CONTENT_URI, null, QuotesContract.QuoteEntry
            .COLUMN_CUSTOM + " = ?", new String[]{"1"}, null);

        return c;
    }

    public void fetchQuotesFromDB() {
        if (context instanceof BaseActivity) {

            ((BaseActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((BaseActivity) context).getLoaderManager().restartLoader(0, null, QuoteDBHelper.this);
                    ((BaseActivity) context).getLoaderManager().initLoader(0, null, QuoteDBHelper.this);
                }
            });

        }
    }

    public void deleteQuote(QuoteObject object) {
        deleteQuote("_id = ?", new String[]{String.valueOf(object.getId())});
    }

    public QuoteObject fetchQuoteById(int id) {
        Cursor c = fetchQuotesCursor(null, "_id = ?", new String[]{String.valueOf(id)}, null);

        c.moveToFirst();

        return new QuoteObject(c);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(context, QuoteProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        ArrayList<QuoteObject> list = new ArrayList<>();

        while (data.moveToNext()) {
            list.add(new QuoteObject(data));
        }

        listener.onDataFinished(list);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void fetchImagesFromOnline() {

        synchronized (LOCK_OBJECT) {
            for (imageRequestCount = 0; imageRequestCount < IMAGE_REQUEST_MAX; imageRequestCount++) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            addImage(Picasso.with(context).load("https://source.unsplash.com/category/nature/400x200").get());
                        } catch (IOException e) {
                            Log.e("MAINFRAGMENT", e.getMessage());
                        }
                    }
                }).start();
            }
        }
    }

    public ScheduleObject getSchedule(long id) {

        Cursor c = db.query(QuotesContract.ScheduleEntry.TABLE_NAME, null, "_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (c.moveToFirst()) {
            return new ScheduleObject(c);
        }
        return null;
    }

    public void setListener(DBHelperCallbackListener listener) {
        this.listener = listener;
    }


    public interface DBHelperCallbackListener {

        //calls when quotes have been read online and added into the data base
        void onLoadOnlineQuotes();

        void onDataFinished(ArrayList<QuoteObject> quotes);
    }

}
