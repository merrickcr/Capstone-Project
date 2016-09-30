package com.example.atv684.positivityreminders.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.atv684.positivityreminders.QuoteObject;

import java.util.Random;

/**
 * Created by atv684 on 9/19/16.
 */
public class QuoteDBHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "quote.db";

    final static int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
            QuotesContract.QuoteEntry.COLUMN_IMAGE + " STRING" +
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

    public void deleteQuoteTable() {

        db.execSQL("DROP TABLE " + QuotesContract.QuoteEntry.TABLE_NAME);

    }


    public void updateQuote(QuoteObject object){

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

    public QuoteObject readQuote() {
        QuoteObject object = null;

        SQLiteDatabase db = getReadableDatabase();

        Random r = new Random();


        Cursor c = db.query(QuotesContract.QuoteEntry.TABLE_NAME, null, null, null, null, null, null);
        c.moveToPosition(r.nextInt(c.getCount()));

        object = new QuoteObject(c);

        return object;

    }
}
