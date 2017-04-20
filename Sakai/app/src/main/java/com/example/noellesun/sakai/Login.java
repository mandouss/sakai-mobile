package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Array;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login extends AppCompatActivity {
    ArrayList<String> idarray = new ArrayList<>();
    private ProgressDialog pDialog;
    private String TAG = Login.class.getSimpleName();
    String target = "site:";
    String cookiestr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WebView browser = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final CookieManager cookieManager = CookieManager.getInstance();

        //Use webview to redirect to sakai login page
        browser.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("MyApp", view.getUrl());

                if (url.equals("https://sakai.duke.edu/portal")) {
                    cookiestr = cookieManager.getCookie(url);
                    new GetMember().execute();
                }
            }
        });

        browser.loadUrl("https://sakai.duke.edu");

    }

    class GetMember extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("https://sakai.duke.edu/direct/membership.json",cookiestr);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray members = jsonObj.getJSONArray("membership_collection");

                    // looping through All memberships
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);

                        String userId = c.getString("userId");//get userId
                        Log.e(TAG, "UserId="+userId);
                        String id = c.getString("id");//userId::site::siteId
                        int startIndex = id.indexOf(target);
                        String siteId = id.substring(startIndex+5);//get siteId
                        Log.e(TAG, "SiteID="+siteId);
                        // adding userId, each siteId into idarray
                        if (i == 0){
                            idarray.add(userId);
                            idarray.add(siteId);
                        }
                        else {
                            idarray.add(siteId);
                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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

                Log.e("ID_ARRAY1:",idarray.toString());
            } else {
                Log.e(TAG, "Couldn't get json from server.");
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
            //send userid and sitesid to sites view
            Intent intent = new Intent(Login.this, sites.class);
            Bundle b=new Bundle();
            b.putStringArrayList("ID_ARRAY",idarray);
            intent.putExtras(b);
            intent.putExtra("ID","Login");
            startActivity(intent);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }

}