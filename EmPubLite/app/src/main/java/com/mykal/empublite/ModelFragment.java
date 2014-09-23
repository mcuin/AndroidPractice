package com.mykal.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;

import java.util.Properties;

/**
 * Created by mykal on 9/23/14.
 */
public class ModelFragment extends Fragment {

    private BookContents contents = null;

    @Override
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
    }

    @Override
    public void run() {
        
    }
}
