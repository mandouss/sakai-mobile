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

public class Lesson extends AppBaseActivity {
    private ProgressDialog pDialog;
    private ListView lv;
    private String siteId;
    private String cookiestr;
    private String lessonURL;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Lesson.GetSites().execute();
    }

    private class GetSites extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Lesson.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            Log.i("show", "message");
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Bundle b = getIntent().getExtras();
            siteId = b.getString("siteId");
            userid = b.getString("USERID");
            HttpHandler sh = new HttpHandler();
            String siteurl = "https://sakai.duke.edu/direct/site/" + siteId + ".json";
            final CookieManager cookieManager = CookieManager.getInstance();
            String cookiestr = cookieManager.getCookie("https://sakai.duke.edu/direct/site/");

            String jsonStr = sh.makeServiceCall(siteurl, cookiestr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray jsonArr = jsonObj.getJSONArray("sitePages");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject item = jsonArr.getJSONObject(i);
                        if (item.getString("title").equals("Lessons")) {
                            lessonURL = item.getString("url");
                            return null;
                        }
                    }

                    if(lessonURL == null){
                        if (pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "This site doesn't have lesson",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                        finish();
                    }
                    else {
                        Log.e("url", lessonURL);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            setContentView(R.layout.activity_lesson);
            WebView webView = (WebView) findViewById(R.id.lesson_WebView);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(lessonURL);
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
}


