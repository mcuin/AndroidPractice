package com.mykal.printingcontent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileOutputStream;
import java.io.IOException;


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

    private void doPrint() {

        PrintManager manager = (PrintManager) getActivity().getSystemService();
        String jobName = getActivity().getString(R.string.app_name) + "Document";

        manager.print(jobName, new MyPrintDocumentAdapter(getActivity(), null));
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal,
    PrintDocumentAdapter.LayoutResultCallback callback, Bundle metaData){

        mPdfDocument = new PrintedPdfDocument(this, newAttributes);

        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }

        int pages = computePageCount(newAttributes);

        if (pages > 0) {
            PrintDocumentInfo info = new PrintDocumentInfo.Builder("print_output.pdf").setContentType(
                    PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).setPageCount(pages).build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    private int computePageCount(PrintAttributes attributes) {
        int itemsPerPage = 4;
        PrintAttributes.MediaSize pageSize = PrintAttributes.getSizeMedia();

        if (!pageSize.isPortrait()) {
            itemsPerPage = 6;
        }

        int printItemCount = getPrintItemCount();

        return (int) Math.ceil(printItemCount / itemsPerPage);
    }

    @Override
    public void onWrite(final PageRange[] pageRanges, final ParcelFileDescriptor destination, final
                        CancellationSignal signal, final PrintDocumentAdapter.WriteResultCallback callback) {

        for (int i = 0; i < totalPages; i++) {

            if (containsPage(pageRanges, i)) {

                writtenPagesArray,append(writtenPagesArray.size(), i);
                PdfDocument.Page page = mPdfDocument.startPage(i);

                if (signal.isCanceled()) {
                    callback.onWriteCancelled();
                    mPdfDocument.close();
                    mPdfDocument = null;
                    return;
                }

                drawPage(page);
                mPdfDocument.finishPage(page);
            }
        } try {

            mPdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            mPdfDocument.close();
            mPdfDocument = null;
        }

        PageRange[] writtenPages = computeWrittenPages();
        callback.onWriteFinished(writtenPages);
    }

    private void drawPage(PdfDocument.Page page) {
        Canvas canvas = page.getCanvas();
        int titleBaseline = 72;
        int leftMargin = 54;
        Paint paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setTextSize(36);
        canvas.drawText("Test Title", leftMargin, titleBaseline, paint);
        paint.setTextSize(11);
        canvas.drawText("Test Paragraph", leftMargin, titleBaseline + 25, paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(100, 100, 172, 172, paint);
    }
}
