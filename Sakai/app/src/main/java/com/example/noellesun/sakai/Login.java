package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
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
    ArrayList<String> idarray;
    private ProgressDialog pDialog;
    private String TAG = Login.class.getSimpleName();
    String target = "site:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        idarray = new ArrayList<>();
        WebView browser = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        browser.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("MyApp", view.getUrl());

                if (url.equals("https://sakai.duke.edu/portal")) {
                    //redirect to sites
                    Intent intent = new Intent(Login.this, sites.class);
                    Bundle b=new Bundle();
                    b.putStringArrayList("ID_ARRAY",idarray);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        browser.loadUrl("https://sakai.duke.edu");
        new GetMember().execute();
    }

    private class GetMember extends AsyncTask<Void, Void, Void> {

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
            String jsonStr = sh.makeServiceCall("https://sakai.duke.edu/direct/membership.json");

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray members = jsonObj.getJSONArray("membership");

                    // looping through All memberships
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject c = members.getJSONObject(i);

                        String userId = c.getString("userId");//get userId
                        String id = c.getString("id");//userId::site::siteId
                        int startIndex = id.indexOf(target);
                        String siteId = id.substring(startIndex+5);//get siteId

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