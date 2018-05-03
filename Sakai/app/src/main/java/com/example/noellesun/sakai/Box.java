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
    private ProgressDialog pDialog;
    private ListView lv;
    private String siteId;
    private String cookiestr;
    private String lessonURL;
    private String userid;
    static String activityLabel = "Box";
    static String activityLabelclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        final CookieManager cookieManager = CookieManager.getInstance();
        String cookiestr = cookieManager.getCookie("https://sakaiboxintegrator.tk");
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall("https://sakai.duke.edu/direct/membership.json",cookiestr);
        Log.e("userinfo", "Response from url: " + jsonStr);

        WebView webView = (WebView) findViewById(R.id.lesson_WebView);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sakaiboxintegrator.tk");
        Log.i("debug", "here!!");
        siteId = getIntent().getExtras().getString("SiteID");
        userid = getIntent().getExtras().getString("USERID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/"+ "Box";
        establish_nav(siteId, userid, activityLabelclick);
        setTitle(activityLabel);

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


