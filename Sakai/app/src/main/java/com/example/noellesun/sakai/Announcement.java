package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class Announcement extends AppCompatActivity {
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
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        lv = (ListView) findViewById(R.id.announcelist);
        m = (Menu) findViewById(R.id.menuId);
        siteid = getIntent().getExtras().getString("SiteID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/" + "Announcements";
        Log.i("ASSIGNiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Announcement.GetAnnounce().execute();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView n = (NavigationView)findViewById(R.id.navi_id);
        n.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                        Intent toSites = new Intent(Announcement.this, sites.class);
                        //
                        startActivity(toSites);
                        return true;
                    }
                }
        );
        setTitle(activityLabel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){

            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Intent toSites = new Intent(Announcement.this, sites.class);
            startActivity(toSites);
        }
    };
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
            String url = fixurl + siteid + ".json";
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
                        String itemName = c.getString("SubjectName");
                        String modifiedTime = c.getString("modifiedTimeString");
                        String createdBy = c.getString("createdByString");
                        String instructions = c.getString("instructions");
                        Log.e("ANNONITEMNAME", itemName);

                        //store the variable needed in a hashmap
                        HashMap<String, String> eachAnnounce = new HashMap<>();
                        eachAnnounce.put("itemName", itemName);
                        eachAnnounce.put("createdBy", createdBy);
                        eachAnnounce.put("modifiedTimeString", modifiedTime);
                        eachAnnounce.put("instructions", instructions);
                        eachAnnounce.put("title", activityLabel);
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
            NavigationView n = (NavigationView)findViewById(R.id.navi_id);
            setTitle(activityLabel);
            n.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            item.setChecked(true);
                            mDrawerLayout.closeDrawers();
//                            Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                            if(item.getTitle().equals("Announcement")){
                                Intent toAnnouncement = new Intent(Announcement.this, Announcement.class);
                                toAnnouncement.putExtra("SiteID",siteid);
                                startActivity(toAnnouncement);
                            }
                            else if(item.getTitle().equals("Gradebook")) {
                                Intent toSites = new Intent(Announcement.this, Gradebook.class);
                                toSites.putExtra("SiteID",siteid);
                                startActivity(toSites);
                            } // not related to Gradebook
                            return true;
                        }
                    }
            );

            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the assignment lists
            ListAdapter adapter = new SimpleAdapter( Announcement.this, annoList,
                    R.layout.announce_listitem, new String[]{"itemName","modifiedTime",
                    "createdBy"},new int[]{R.id.itemName, R.id.modifiedTime, R.id.createdBy});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Announcement.this, eachAnnounce.class);
                    //send the assignment info to each Assign view
                    intent.putExtra("assign info",annoList.get(position));
                    intent.putExtra("activityLabelclick", activityLabelclick);
                    startActivity(intent);
                }
            });

        }
    }
}