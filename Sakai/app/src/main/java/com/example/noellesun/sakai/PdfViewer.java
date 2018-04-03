package com.example.noellesun.sakai;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class PdfViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.web_layout);
//        WebView webView = (WebView) findViewById(R.id.webview);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setJavaScriptEnabled(true);
//        String googleDocs = "https://docs.google.com/viewer?url=";
//        webView.loadUrl(googleDocs + "https://people.duke.edu/~bmr23/ece650/homework/hw1.pdf");

        setContentView(R.layout.web_layout);
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        String url = "https://people.duke.edu/~bmr23/ece650/homework/hw4.pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        dm.enqueue(request);
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
}
