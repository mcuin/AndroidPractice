package com.mykal.displaybitmapefficiently;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


/**
 * Created by mykal on 3/20/15.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private int data = 0;
    private MainActivity activity = new MainActivity();

    public BitmapWorkerTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }


    protected Bitmap doInBackground(Context context, Integer... params) {
        data = params[0];
        return activity.decodeSampleBitmapFromResource(context.getResources(), data, 100, 100);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

            if (this == bitmapWorkerTask && imageView != null) {

                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void loadBitmap(int resId, ImageView imageView, Context context) {

        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            final AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);

            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;

            if (bitmapData == 0 || bitmapData != data) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }

        return  true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {

        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();

            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;

                return asyncDrawable.getBitmapWorkerTask();
            }
        }
    }
}
