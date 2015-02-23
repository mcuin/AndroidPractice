package com.mykal.sharingsimpledata;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleText(intent);
            } else if ((type.startsWith("image/"))) {
                handleBinary(intent);
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                if (type.startsWith("image/")) {
                    handleMult(intent);
                }
            }
        }
    }

    public void sendText() {
        Intent textIntent = new Intent();
        textIntent.setAction(Intent.ACTION_SEND);
        textIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        textIntent.setType("text/plain");
        startActivity(Intent.createChooser(textIntent, getResources().getText(R.string.send_to)));
    }

    public void sendBinary() {
        Intent binaryIntent = new Intent();
        binaryIntent.setAction(Intent.ACTION_SEND);
        binaryIntent.putExtra(Intent.EXTRA_STREAM, image);
        binaryIntent.setType("image/jpg");
        startActivity(Intent.createChooser(binaryIntent, getResources().getString(R.string.send_to)));
    }

    public void sendMult() {
        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        imageUris.add(imageUri1);
        imageUris.add(imageUri2);

        Intent multIntent = new Intent();
        multIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        multIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        multIntent.setType("image/*");
        startActivity(Intent.createChooser(multIntent, "Share Images to: "));
    }

    public void handleText(Intent intent) {
        String shared = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (shared != null) {

        }
    }

    public void handleBinary(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (shared != null) {

        }
    }

    public void  handleMult(Intent intent) {
        ArrayList<Uri> imageUri = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

        MenuItem item = menu.findItem(R.id.menu_item_share);
        mSharedActionProvider = (SharedActionProvider) item.getActionProvider();

        return true;
    }

    public void setShareIntent(Intent shareIntent) {
        if (mSharedActionProvider != null) {
            mSharedActionProvider.setShareIntent(shareIntent);
        }
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
}
