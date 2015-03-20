package com.mykal.displaybitmapefficiently;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ResourceBundle;


public class MainActivity extends Activity {

    BitmapFactory.Options options = new BitmapFactory.Options();
    int imageHeight = options.outHeight;
    int imageWidth = options.outWidth;
    String imageType = options.outMimeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.id.myimage, options);
        mImageView.setImageBitmap(decodeSampleBitmapFromResource(getResources(), R.id.myimage, 100, 100));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static int calcInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int sampleSize = 1;

        if (height > reqHeight || width > reqHeight) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth) {
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }

    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int resId, int reqWidth,
                                                        int reqHeight) {
        final BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, resId, options1);
        options1.inSampleSize = calcInSampleSize(options1, reqWidth, reqHeight);

        options1.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options1);
    }
}
