package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Locale;


public class Announcement extends AppBaseActivity {
    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> annoList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
    private Menu m;
    String fixurl = "https://sakai.duke.edu/direct/announcement/site/";
    String cookiestr;
    String siteid;
    static String activityLabel = "Announcements";
    static String activityLabelclick ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        lv = (ListView) findViewById(R.id.announcelist);
        siteid = getIntent().getExtras().getString("SiteID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/" + "Announcements";
        establish_nav(siteid, activityLabelclick);
        Log.i("ASSIGNiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Announcement.GetAnnounce().execute();
        setTitle(activityLabel);
    }

//    final OnClickListener siteClickEvent = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Toast.makeText(getApplicationContext(),"sites clicked",Toast.LENGTH_SHORT).show();
//        }
//    };
//    //redirect to sites
//    final OnClickListener sitesclick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent toSites = new Intent(Announcement.this, sites.class);
//            startActivity(toSites);
//        }
//    };
    // AsuncTask that is used to get json from url
    private class GetAnnounce extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Announcement.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = fixurl + siteid + ".json?n=100&d=3000";
            Log.i("announce_url",url);
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            Log.e(TAG, "ANNOUNCEJSON: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray announcements = jsonObj.getJSONArray("announcement_collection");
                    for (int i = 0; i < announcements.length(); i++) {
                        JSONObject c = announcements.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("title");
                        //String modifiedTime = //timestring not displayed, i.e 1521740259718 ->3/22/2018, 1:37:39 PM
                        long timestamp = c.getInt("createdOn");
                        Date modifiedTime = new Date(timestamp);
                        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm aaa", Locale.US);
                        //Date today = Calendar.getInstance().getTime();
                        //String reportDate = df.format(today);
                        // format: "display": "Jan 12, 2018 7:00 pm",
                        // current: 01/21/1970 00:54:21
                        String modifiedTimeString = df.format(modifiedTime);
                        String millisecTimeString = new Long(timestamp).toString();

                        String createdBy = c.getString("createdByDisplayName");
                        String instructions = c.getString("body");
                        String resource_url = c.getString("entityURL");
                        Log.e("ANNONITEMNAME", itemName);

                        //store the variable needed in a hashmap
                        HashMap<String, String> eachAnnounce = new HashMap<>();
                        eachAnnounce.put("itemName", itemName);
                        eachAnnounce.put("createdBy", createdBy);
                        //eachAnnounce.put("modifiedTimeString", reportDate);   // modifiedTimeString=04/02/2018 17:10:33
                        eachAnnounce.put("modifiedTimeString", modifiedTimeString);
                        eachAnnounce.put("millisecTimeString", millisecTimeString);
                        eachAnnounce.put("instructions", instructions);
                        eachAnnounce.put("title", activityLabel);
                        eachAnnounce.put("resource_url", resource_url);
                        annoList.add(eachAnnounce);
                        Log.i("ANNOLIST",annoList.toString());
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
            Log.e("background","done!");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            setTitle(activityLabel);
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the announcement lists
            ListAdapter adapter = new SimpleAdapter( Announcement.this, annoList,
                    R.layout.announce_listitem, new String[]{"itemName","modifiedTimeString",
                    "createdBy"},new int[]{R.id.itemName, R.id.modifiedTime, R.id.createdBy});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Announcement.this, eachAnnounce.class);
                    //send the announcement info to each Announ view
                    intent.putExtra("Announce info",annoList.get(position));
                    startActivity(intent);
                }
            });

        }
    }
}