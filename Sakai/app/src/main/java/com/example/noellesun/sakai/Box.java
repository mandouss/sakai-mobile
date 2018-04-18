package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Box extends AppBaseActivity {
    private String cookiestr;
    private String lessonURL;
    String siteId;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        WebView webView = (WebView) findViewById(R.id.lesson_WebView);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        siteId = getIntent().getExtras().getString("SiteID");
        userid = getIntent().getExtras().getString("USERID");
        webView.loadUrl("https://duke.app.box.com/embed_widget/files/0/f/0");
        Log.i("debug", "here!!");
        establish_nav(siteId, userid, "");

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("MyApp", view.getUrl());
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                return false; // then it is not handled by default action
            }
        });
    }
}


