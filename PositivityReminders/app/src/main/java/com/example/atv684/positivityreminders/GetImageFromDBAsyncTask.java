package com.example.atv684.positivityreminders;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;


public class GetImageFromDBAsyncTask extends AsyncTask<Object, Integer, Bitmap> {


    private final Context context;

    public GetImageFromDBAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Object... object) {
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromResource(QuoteDBHelper.get(context).getImage(), 800, 600);

        if (bitmap != null) {
            return bitmap;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }


}
