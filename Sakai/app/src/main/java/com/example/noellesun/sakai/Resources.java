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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Resources extends AppBaseActivity {

    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> resList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
//    String fixurl = "https://sakai.duke.edu/access/content/group/";
    String fixurl = "https://sakai.duke.edu/direct/content/site/";
    String cookiestr;
    String siteid;
    static String activityLabel = "Resources";
    static String activityLabelclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        lv = (ListView) findViewById(R.id.resourcelist);
        siteid = getIntent().getExtras().getString("SiteID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/" + "Resources";
        Log.i("RESOURiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Resources.GetResour().execute();

        //});

        establish_nav(siteid, activityLabelclick);
        setTitle(activityLabel);

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
                    //title = resources.getJSONObject(0).getString("entityTitle") + " / " + "Resources";
                    for (int i = 0; i < resources.length(); i++) {
                        JSONObject c = resources.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("entityTitle");
                        String numChildren = c.getString("numChildren");
                        String createdBy = c.getString("author");
                        String resource_url = c.getString("url");
                        String type = c.getString("type");
                        String size = "";
                        if(!numChildren.equals("0")){
                            //size = "Dir";
                        }
                        else {
                            size = c.getString("size");
                            int sz = Integer.parseInt(size);
                            if (sz > 1024 * 1024) {
                                double num = sz/(1024 * 1024);
                                size = Double.toString(num) + " MB";
                            }
                            else if (sz > 1024) {
                                double num = sz/(1024);
                                size = Double.toString(num) + " KB";
                            }
                            else{
                                size = Double.toString(sz) + " B";
                            }
                        }
                        Log.e("RESOURCEMNAME", itemName);

                        //store the variable needed in a hashmap
                        HashMap<String, String> eachResource = new HashMap<>();
                        eachResource.put("itemName", itemName);
                        eachResource.put("numChildren", numChildren);
                        eachResource.put("createdBy", createdBy);
                        eachResource.put("resource_url", resource_url);
                        eachResource.put("type", type);
                        eachResource.put("size", size);
                        //eachResource.put("title", title);
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
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            setTitle(activityLabel);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the resources lists
            final ListAdapter adapter = new SimpleAdapter(Resources.this, resList,
                    R.layout.resource_listitem, new String[]{"itemName", "size",
                    "createdBy"}, new int[]{R.id.itemName, R.id.size, R.id.createdBy});
            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    if (!resList.get(position).get("type").equals("collection")) {
                        Intent intent = new Intent(Resources.this, eachResource.class);
                        //send the resource info to each Resource view
                        intent.putExtra("resource info", resList.get(position));
                        intent.putExtra("activityLabelclick", activityLabelclick);
                        //                    intent.putExtra("resource_name", );
                        //intent.putExtra("resource info", resList);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"This is a directory, you can't download it", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
