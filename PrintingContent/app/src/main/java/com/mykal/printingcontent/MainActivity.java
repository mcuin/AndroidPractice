package com.mykal.printingcontent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void doPhotoPrint() {
        PrintHelper photoPrint = new PrintHelper(getActivity());

        photoPrint.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.droids);

        photoPrint.printBitmap("droids.jpg - test print", bitmap);
    }

    private void doWebPrint() {
        WebView webView = new WebView(getActivity());

        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "page finished loading" + url);
                createWebPrintJob(view);
                mWebView = null;
            }
        });

        String htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, " + "testing, testing..." +
                "</p></body></html>";

        webView.loadDataWithBaseURL("file:///android_assets/images/", htmlDocument, "text/HTML", "UTF-8", null);

        mWebView = webView;
    }

    private void createWebPrintJob(WebView webView) {

        PrintManager manager = (PrintManager) getActivity.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter adapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + "Document";
        PrintJob job = manager.print(jobName, adapter, new PrintAttributes.Builder().build());

        mJob.add(job);
    }
}
