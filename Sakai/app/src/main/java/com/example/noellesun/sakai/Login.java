package com.example.noellesun.sakai;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView browser = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        browser .setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view,url,favicon);
                Log.i("MyApp", view.getUrl());

                if(url.equals("https://sakai.duke.edu/portal")){
                    //redirect to sites
                    startActivity(new Intent(Login.this,sites.class));
                }
            }
        });

        browser.loadUrl("https://sakai.duke.edu");

    }
}
