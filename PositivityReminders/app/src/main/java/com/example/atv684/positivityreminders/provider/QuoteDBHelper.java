package com.example.atv684.positivityreminders.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.atv684.positivityreminders.QuoteObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by atv684 on 9/19/16.
 */
public class QuoteDBHelper extends SQLiteOpenHelper implements Callback {

    final static String DATABASE_NAME = "quote.db";

    final static int DATABASE_VERSION = 2;

    private final Context context;

    private int requestCount = 0;

    private static final int REQUEST_COUNT_MAX = 3;

    private SQLiteDatabase db;

    DBHelperCallbackListener listener;

    private ArrayList<Request> requestQueue = new ArrayList<Request>();

    public QuoteDBHelper(Context context, DBHelperCallbackListener listener) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
        this.listener = listener;
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addQuote(String text, String author, String imageUri) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, author);
        values.put(QuotesContract.QuoteEntry.COLUMN_TEXT, text);
        values.put(QuotesContract.QuoteEntry.COLUMN_IMAGE, imageUri);
        values.put(QuotesContract.QuoteEntry.COLUMN_CUSTOM, 1);

        db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);
    }

    public void addQuote(QuoteObject quote) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QuotesContract.QuoteEntry.COLUMN_AUTHOR, quote.getAuthor());
        values.put(QuotesContract.QuoteEntry.COLUMN_TEXT, quote.getText());

        try {
            db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);
        } catch (Exception e) {

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

    public Cursor fetchQuotesCursor(String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = getReadableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(QuotesContract.QuoteEntry.TABLE_NAME);

        Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return c;
    }


    public void fetchQuotesFromOnline() {

        OkHttpClient client = new OkHttpClient();

        requestCount = 0;
        for (int i = 0; i < REQUEST_COUNT_MAX; i++) {
            try {

                Request request = new Request.Builder()
                    .url("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en")
                    .build();

                client.newCall(request).enqueue(this);

                synchronized (requestQueue) {
                    requestQueue.add(request);
                }
            } catch (Exception e) {
                Log.e("DBHelper", e.getMessage());
            }
        }


    }

    @Override
    public void onFailure(Call call, IOException e) {
        //do nothing?
        Log.e("DB REQUEST", "FAILED");

        synchronized (requestQueue) {
            requestQueue.remove(call.request());
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Handler handler = new Handler(Looper.getMainLooper());

        final Call finalCall = call;
        final Response finalResponse = response;

        QuoteObject quote = new QuoteObject(finalResponse.body().string());

        //String urlImage = getImageForQuote();

        addQuote(quote);

        synchronized (requestQueue) {
            requestQueue.remove(finalCall.request());
        }

        handler.post(new Runnable() {

            @Override
            public void run() {
                if (listener != null && requestQueue.isEmpty()) {
                    listener.onLoadOnlineQuotes();
                }
            }
        });
    }

    private String getImageForQuote() {
        //https://source.unsplash.com/category/nature/800x600

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("https://source.unsplash.com/category/nature/800x600")
            .build();

        try {
            Response response = client.newCall(request).execute();

            Log.e("TAG", response.body().toString());
        } catch (IOException e) {
            return null;
        }

        return null;
    }

    public long insertQuote(ContentValues values) {

        SQLiteDatabase db = getWritableDatabase();

        return db.insert(QuotesContract.QuoteEntry.TABLE_NAME, null, values);
    }

    public int deleteQuote(String selection, String[] selectionArgs){

        db = getWritableDatabase();

        return db.delete(QuotesContract.QuoteEntry.TABLE_NAME, selection, selectionArgs);

    }

    public Cursor fetchFavoriteQuotes(){

        Cursor c = context.getContentResolver().query(QuoteProvider.CONTENT_URI, null, QuotesContract.QuoteEntry
            .COLUMN_FAVORITE + " = ?", new String[] {"1"}, null);

        return c;
    }

    public Cursor fetchQuotesFromDB() {
        return context.getContentResolver().query(QuoteProvider.CONTENT_URI, null, null, null, null);
    }

    public interface DBHelperCallbackListener {

        //calls when quotes have been read online and added into the data base
        void onLoadOnlineQuotes();
    }


}
