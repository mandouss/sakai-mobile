package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class Lesson extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        WebView webView = (WebView) findViewById(R.id.lesson_WebView);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://sakai.duke.edu/portal/site/");
    }
}
