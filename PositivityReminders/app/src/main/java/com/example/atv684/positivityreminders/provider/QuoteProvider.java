package com.example.atv684.positivityreminders.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.atv684.positivityreminders.ImageUtil;
import com.example.atv684.positivityreminders.QuoteObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created Chris on 9/19/16.
 */
public class QuoteProvider extends ContentProvider implements QuoteDBHelper.DBHelperCallbackListener {

    public static final String PROVIDER_NAME = "positivereminders.provider.quotes";

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/quotes");

    public static final Uri IMAGE_URI = Uri.parse("content://" + PROVIDER_NAME + "/images");

    public static final int QUOTES = 1;

    public static final int QUOTE_ID = 2;

    private static final int FAVORITE_QUOTES = 2;

    private static final int SAVED_QUOTES = 3;

    private static final int IMAGE_ID = 4;

    private QuoteDBHelper dbHelper = null;

    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "quotes", QUOTES);
        uriMatcher.addURI(PROVIDER_NAME, "quotes/#", QUOTE_ID);
        uriMatcher.addURI(PROVIDER_NAME, "images/*", IMAGE_ID);
        return uriMatcher;
    }

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
            case IMAGE_ID:
                args = new String[]{uri.getPathSegments().get(1)};
                return dbHelper.fetchImageCursor(null, QuoteDBHelper.IMAGE_KEY_NAME + " = ?", args, null);
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

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {

        Log.v("QA", "Called with uri: '" + uri + "'." + uri.getLastPathSegment());

        // Check incoming Uri against the matcher
        switch (getUriMatcher().match(uri)) {

            // If it returns 1 - then it matches the Uri defined in onCreate
            case IMAGE_ID:

                // The desired file name is specified by the last segment of the
                // path
                // E.g.
                // 'content://com.stephendnicholas.gmailattach.provider/Test.txt'
                // Take this and build the path to the file
                // String fileLocation = getContext().getCacheDir() + File.separator + uri.getLastPathSegment();

//                String[] args = new String[]{uri.getPathSegments().get(1)};
//                Cursor c = dbHelper.fetchImageCursor(null, QuoteDBHelper.IMAGE_KEY_NAME + " = ?", args, null);
//
//                Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(c.getBlob(c.getColumnIndex(QuoteDBHelper.IMAGE_KEY_IMAGE)), 800, 600);

                final ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor( uri, "r");

                return parcelFileDescriptor;

            // Otherwise unrecognised Uri
            default:
                Log.v("PROVIDER", "Unsupported uri: '" + uri + "'.");
                throw new FileNotFoundException("Unsupported uri: " + uri.toString());
        }
    }
}
