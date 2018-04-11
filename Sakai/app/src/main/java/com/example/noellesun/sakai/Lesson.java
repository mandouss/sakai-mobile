package com.example.noellesun.sakai;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class Lesson extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        String siteId = b.getString("siteId");
        String userid = b.getString("USERID");
        String lessonURL = "https://sakai.duke.edu/portal/site/" + siteId + "/tool/1259d18b-8d95-4bd9-9aa8-4a0f3acf6176";
        setContentView(R.layout.activity_lesson);
        WebView webView = (WebView) findViewById(R.id.lesson_WebView);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        final CookieManager cookieManager = CookieManager.getInstance();
        String cookiestr = cookieManager.getCookie(lessonURL);
        Map<String, String> abc = new HashMap<String, String>();
        abc.put("Cookie", cookiestr);
        CookieSyncManager.getInstance().sync();
        webView.loadUrl(lessonURL, abc);
        establish_nav(siteId, userid, "");

        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("MyApp", view.getUrl());


            }
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url

                return false; // then it is not handled by default action
            }
        });

    }
}
