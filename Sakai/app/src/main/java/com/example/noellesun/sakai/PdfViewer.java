package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.content.SharedPreferences;

public class PdfViewer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        String res_url = (String)getIntent().getStringExtra("url_str");
        String googleDocs = "https://docs.google.com/viewer?url=";
        webView.loadUrl(googleDocs + "https://people.duke.edu/~bmr23/ece650/homework/hw1.pdf");
    }
}
