package com.example.noellesun.sakai;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.CookieManager;

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
        String url = "https://sakai.duke.edu/access/content/group/40068560-0072-4822-b62f-89f3f3d3e900/ECE%20651%20Software%20Engineering.pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        final CookieManager cookieManager = CookieManager.getInstance();
        String cookiestr = cookieManager.getCookie(url);
        request.addRequestHeader("Cookie", cookiestr);

        dm.enqueue(request);
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }
}
