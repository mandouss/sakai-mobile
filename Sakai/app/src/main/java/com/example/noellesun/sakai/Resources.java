package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Resources extends AppCompatActivity {

    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> resList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
//    String fixurl = "https://sakai.duke.edu/access/content/group/";
    String fixurl = "https://sakai.duke.edu/direct/content/site/";
    String cookiestr;
    String siteid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        lv = (ListView) findViewById(R.id.resourcelist);
        siteid = getIntent().getExtras().getString("SiteID");
        Log.i("RESOURiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Resources.GetResour().execute();

    }
    final OnClickListener siteClickEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(),"sites clicked",Toast.LENGTH_SHORT).show();
        }
    };
    //redirect to sites
    final OnClickListener sitesclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toSites = new Intent(Resources.this, sites.class);
            startActivity(toSites);
        }
    };
    // AsuncTask that is used to get json from url
    private class GetResour  extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Resources.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {  // String... urls
            HttpHandler sh = new HttpHandler();
            String url = fixurl + siteid + ".json";
            Log.i("resource_url", url);
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            Log.e(TAG, "RESOURCEJSON: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray resources = jsonObj.getJSONArray("content_collection");
                    for (int i = 0; i < resources.length(); i++) {
                        JSONObject c = resources.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("entityTitle");
                        String modifiedTime = c.getString("modifiedDate");
                        String createdBy = c.getString("author");
                        String resourceurl = c.getString("url");
                        String access = c.getString("usage");
                        Log.e("RESOURCEMNAME", itemName);

                        //store the variable needed in a hashmap
                        HashMap<String, String> eachResource = new HashMap<>();
                        eachResource.put("itemName", itemName);
                        eachResource.put("modifiedTime", modifiedTime);
                        eachResource.put("createdBy", createdBy);
                        eachResource.put("resourceurl", resourceurl);
                        eachResource.put("access", access);
                        resList.add(eachResource);
                        Log.i("RESLIST", resList.toString());
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
            Log.e("background", "done!");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the resources lists
            ListAdapter adapter = new SimpleAdapter(Resources.this, resList,
                    R.layout.resource_listitem, new String[]{"itemName", "modifiedTime",
                    "createdBy"}, new int[]{R.id.itemName, R.id.modifiedTime, R.id.createdBy});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Resources.this, eachResource.class);
                    //send the resource info to each Resource view
                    intent.putExtra("resource info", resList.get(position));
                    startActivity(intent);
                }
            });

        }

    }
}
