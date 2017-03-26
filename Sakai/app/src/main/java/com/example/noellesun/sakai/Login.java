package com.example.noellesun.sakai;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView browser = (WebView) findViewById(R.id.webview);
        browser .setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // 
            }
        });

        browser.loadUrl("https://sakai.duke.edu");

    }
}
