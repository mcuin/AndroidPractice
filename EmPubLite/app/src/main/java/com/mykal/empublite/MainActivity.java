package com.mykal.empublite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private ViewPager pager = null;
    private ContentsAdapter adapter = null;
    private static final String MODEL = "model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new ContentsAdapter(this);
        pager.setAdapter(adapter);
        findViewById(R.id.progressBar1).setVisibility(View.GONE);
        findViewById(R.id.pager).setVisibility(View.VISIBLE);
        ModelFragment mfrag = (ModelFragment)getFragmentManager().findFragmentByTag(MODEL);

        if(mfrag == null) {
            getFragmentManager().beginTransaction().add(new ModelFragment(), MODEL).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return (true);

            case R.id.about:
                Intent i = new Intent(this, SimpleContentActivity.class);
                i.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/about.html");
                startActivity(i);

                return (true);

            case R.id.help:
                i = new Intent(this, SimpleContentActivity.class);
                i.putExtra(SimpleContentActivity.EXTRA_FILE, "file:///android_asset/misc/help.html");
                startActivity(i);

                return (true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }
}
