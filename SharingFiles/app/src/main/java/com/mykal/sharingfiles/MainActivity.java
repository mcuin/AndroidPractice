package com.mykal.sharingfiles;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;


public class MainActivity extends ActionBarActivity {

    private File mPrivateRootDir;
    private File mImagesDir;
    File[] mImageFiles;
    String[] mImageFilenames;
    private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
        Intent mResultIntent = new Intent("com.mykal.ACTION_RETURN_FILE");
        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();
        setResult(Activity.RESULT_CANCELED, null);

        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener()) {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                File requestFile = new File(mImageFilename[position]);

                try {
                    fileUri = FileProvider.getUriForFile(MainActivity.this, "com.mykal.fileprovider", requestFile);
                } catch(IllegalArgumentException e) {
                    Log.e("File Selector", "The selected file can't be shared: " + clickedFilename);
                }
            }

            if (fileUri != null) {
                mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));

                MainActivity.this.setResult(Activity.RESULT_OK, mResultIntent);
            } else {
                mResultIntent.setDataAndType(null, "");
                MainActivity.this.setResult(RESULT_CANCELED, mResultIntent);
            }
        }
    }

    public void onDoneClick(View v) {
        finish();
    }

    public void requestFile() {

        startActivityForResult(mRequestFileIntent, 0);
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

    @Override
    public void onActvivityResult(int requestCode, int resultCode, Intent returnIntent) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            Uri returnUri = returnIntent.getData();
            try {
                mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActvity", "File Not Found");
                return;
            }

            FileDescriptor fd = mInputPFD.getFileDescriptor();
        }
    }
}
