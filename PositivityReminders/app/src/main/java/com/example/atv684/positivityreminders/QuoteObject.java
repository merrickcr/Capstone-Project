package com.example.atv684.positivityreminders;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.atv684.positivityreminders.provider.QuotesContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created Chris on 9/16/16.
 */
public class QuoteObject implements Serializable{

    private String text;

    private String author;

    private int id;

    private int numViews;

    private Bitmap image;

    private boolean isFavorite = false;

    private boolean isCustom = false;

    private String imageURI = "";

    public QuoteObject(Cursor c) {

        if (c.getCount() <= 0) {
            return;
        }

        text = c.getString(c.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_TEXT));
        author = c.getString(c.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_AUTHOR));
        numViews = c.getInt(c.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_NUM_VIEWS));
        isCustom = (c.getInt(c.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_CUSTOM)) > 0) ? true : false;
        isFavorite = (c.getInt(c.getColumnIndex(QuotesContract.QuoteEntry.COLUMN_FAVORITE)) > 0) ? true : false;
        id = c.getInt(c.getColumnIndex(QuotesContract.QuoteEntry._ID));

    }

    public QuoteObject(String jsonString) {

        try {
            JSONObject json = new JSONObject(jsonString);

            this.text = json.optString("quoteText");
            this.author = json.optString("quoteAuthor");

        } catch (JSONException e) {
            Log.e("QUOTE", e.getMessage());
        }


    }

    public QuoteObject(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumViews() {
        return numViews;
    }

    public void setNumViews(int numViews) {
        this.numViews = numViews;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(boolean custom) {
        isCustom = custom;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public void incrementViews() {
        numViews += 1;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
