package com.example.noellesun.sakai;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.content.SharedPreferences;
import com.github.barteksc.pdfviewer.PDFView;


public class PdfViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        String res_url = (String)getIntent().getStringExtra("url_str");
        //String googleDocs = "https://docs.google.com/viewer?url=";
        //webView.loadUrl(googleDocs + "https://people.duke.edu/~bmr23/ece650/homework/hw1.pdf");

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(res_url);
        intent.setData(content_url);
        startActivity(intent);

       }
}
