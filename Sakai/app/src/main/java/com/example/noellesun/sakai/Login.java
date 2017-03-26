package com.example.noellesun.sakai;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView browser = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        browser .setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url) {
                Log.i("MyApp", view.getUrl());
                if(view.getUrl() == "https://sakai.duke.edu/portal"){
                    //redirect to sites

                }
            }
        });

        browser.loadUrl("https://sakai.duke.edu");

    }
}
