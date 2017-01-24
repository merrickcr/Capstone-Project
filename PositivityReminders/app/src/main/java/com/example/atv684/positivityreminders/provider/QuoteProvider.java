package com.example.atv684.positivityreminders.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.atv684.positivityreminders.QuoteObject;

import java.util.ArrayList;

/**
 * Created by atv684 on 9/19/16.
 */
public class QuoteProvider extends ContentProvider implements QuoteDBHelper.DBHelperCallbackListener {

    public static final String PROVIDER_NAME = "positivereminders.provider.quotes";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/quotes");

    public static final int QUOTES = 1;

    public static final int QUOTE_ID = 2;

    private static final int FAVORITE_QUOTES = 2;

    private static final int SAVED_QUOTES = 3;

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "quotes", QUOTES);
        uriMatcher.addURI(PROVIDER_NAME, "quotes/#", QUOTE_ID);
        return uriMatcher;
    }


    private QuoteDBHelper dbHelper = null;

    @Override
    public boolean onCreate() {
        dbHelper = QuoteDBHelper.get(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (getUriMatcher().match(uri)) {
            case QUOTES:
                return dbHelper.fetchQuotesCursor(projection, selection, selectionArgs, sortOrder);
            case QUOTE_ID:
                String[] args = new String[]{uri.getPathSegments().get(1)};
                return dbHelper.fetchQuotesCursor(null, "_id = ?", args, null);
        }

        return null;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (getUriMatcher().match(uri)) {
            case QUOTES:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (getUriMatcher().match(uri)) {
            case QUOTES:
                return ContentUris.withAppendedId(CONTENT_URI, dbHelper.insertQuote(values));
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (getUriMatcher().match(uri)) {
            case QUOTE_ID:
                return dbHelper.deleteQuote(selection, selectionArgs);
            default:
                return -1;
        }

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        switch (getUriMatcher().match(uri)) {
            case QUOTE_ID:
                return dbHelper.updateQuote(values, selection, selectionArgs);
            default:
                return -1;
        }
    }

    @Override
    public void onLoadOnlineQuotes() {
        //not needed, ignore
    }

    @Override
    public void onDataFinished(ArrayList<QuoteObject> quotes) {
        //not needed
    }
}
