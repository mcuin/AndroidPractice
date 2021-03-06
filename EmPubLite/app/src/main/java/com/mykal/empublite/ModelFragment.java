package com.mykal.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import de.greenrobot.event.EventBus;

/**
 * Created by mykal on 9/23/14.
 */
public class ModelFragment extends Fragment {

    private BookContents contents = null;

    public void OnCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onAttach(Activity host) {
        super.onAttach(host);

        if (contents == null) {
            new LoadThread(host.getAssets()).start();
        }
    }

    public BookContents getBooks() {
        return(contents);
    }

    private class LoadThread extends Thread {

        private AssetManager assets = null;

        LoadThread(AssetManager assets) {
            super();
            this.assets = assets;
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        }

        @Override
        public void run() {
            Gson gson = new Gson();

            try {
                InputStream is = assets.open("book/contents.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                contents = gson.fromJson(reader, BookContents.class);
                EventBus.getDefault().post(new BookLoadedEvent(contents));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception parsing JSON", e);
            }
        }
    }
}
