package com.mykal.empublite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                startActivity(i);

                return (true);

            case R.id.help:
                i = new Intent(this, SimpleContentActivity.class);
                startActivity(i);

                return (true);
        }
        return(super.onOptionsItemSelected(item));
    }
}
