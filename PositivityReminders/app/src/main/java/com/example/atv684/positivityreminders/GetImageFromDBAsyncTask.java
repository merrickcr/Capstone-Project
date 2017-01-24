package com.example.atv684.positivityreminders;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.atv684.positivityreminders.provider.QuoteDBHelper;

public class GetImageFromDBAsyncTask extends AsyncTask<Object, Integer, Bitmap> {


    private final Context context;

    public GetImageFromDBAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(Object... object) {
        Bitmap bitmap = decodeSampledBitmapFromResource(QuoteDBHelper.get(context).getImage(), 800, 600);

        if(bitmap != null){
            return bitmap;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }


    /**
     * Bitmap resize taken from Googl ceveloper docs
     *
     * https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] bitmap,
        int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        if(bitmap == null){
            return null;
        }
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length, options);
    }
}
