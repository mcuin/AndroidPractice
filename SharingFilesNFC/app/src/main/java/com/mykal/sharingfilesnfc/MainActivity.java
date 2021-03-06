package com.mykal.sharingfilesnfc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


public class MainActivity extends Activity {

    NfcAdapter mNfcAdapter;
    boolean mAndroidBeam = false;
    private Uri[] mFileUris = new Uri[10];
    String transferFile = "transferimage.jpg";
    File extDir = getExternalFilesDir(null);
    File requestFile = new File(extDir, transferFile);
    private FileUriCallback mFileUriCallback;
    PackageManager manager;
    private String mParentPAth;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestFile.setReadable(true, false);
        Uri fileUri = Uri.fromFile(requestFile);

        if (fileUri != null) {
            mFileUris[0] = fileUri;
        } else {
            Log.e("My Activty", "No File URI available for file");
        }

        if (!manager.hasSystemFeature(PackageManager.FEATURE_NFC)) {

        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mAndroidBeam = false;
        } else {
            mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
            mFileUriCallback = new FileUriCallback();
            mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback, this);
        }
    }

    @TargetApi(16)
    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {

        }

        @Override
        public Uri[] createBeamUris (NfcEvent event) {
            return mFileUris;
        }
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

    private void handleViewIntent() {
        mIntent = getIntent();
        String action = mIntent.getAction();

        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            Uri beamUri = mIntent.getData();

            if (TextUtils.equals(beamUri.getScheme(), "file")) {
                mParentPAth = handleFileUri(beamUri);
            } else if (TextUtils.equals(beamUri.getScheme(), "content")) {
                mParentPAth = handleContentUri(beamUri);
            }
        }
    }

    public String handleFileUri(Uri beamUri) {
        String filename = beamUri.getPath();
        File copiedFile = new File(filename);
        return copiedFile.getParent();
    }

    public String handleContentUri(Uri beamUri) {
        int filenameIndex;
        File copiedFile;
        String filename;

        if (!TextUtils.equals(beamUri.getAuthority(), MediaStore.AUTHORITY)) {

        } else {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursorPath = getContentResolver().query(beamUri, projection, null, null, null);

            if (cursorPath != null && cursorPath.moveToFirst()) {
                filenameIndex = cursorPath.getColumnIndex(MediaStore.MediaColumns.DATA);
                filename = cursorPath.getString(filenameIndex);
                copiedFile = new File(filename);
                return new String(copiedFile.getParent());
            } else {
                return null;
            }
        }
    }
}
