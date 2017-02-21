package com.example.atv684.positivityreminders;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

import java.util.HashMap;


public class GetImageFromDBAsyncTask extends AsyncTask<Object, Integer, HashMap<String, Bitmap>> {


    private final Context context;

    public GetImageFromDBAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected HashMap<String, Bitmap> doInBackground(Object... object) {


        ContentValues cv = QuoteDBHelper.get(context).getImage();
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(cv.getAsByteArray(QuoteDBHelper.IMAGE_KEY_IMAGE), 800, 600);

        HashMap values = new HashMap<String,Bitmap>();
        values.put(cv.getAsString(QuoteDBHelper.IMAGE_KEY_NAME), bitmap);

        if (bitmap != null) {
            return values;
        }

        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, Bitmap> values) {
        super.onPostExecute(values);
    }


}
