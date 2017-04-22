package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Assignment extends AppCompatActivity {

    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> asnList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
    String fixurl = "https://sakai.duke.edu/direct/assignment/site/";
    String cookiestr;
    String siteid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        lv = (ListView) findViewById(R.id.assignlist);
        siteid = getIntent().getExtras().getString("SiteID");
        Log.i("ASSIGNiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Assignment.GetAssign().execute();

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
            Intent toSites = new Intent(Assignment.this, sites.class);
            startActivity(toSites);
        }
    };
    // AsuncTask that is used to get json from url
    private class GetAssign extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Assignment.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = fixurl + siteid + ".json";
            Log.i("assign_url",url);
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            Log.e(TAG, "ASSIGNJSON: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray assignments = jsonObj.getJSONArray("assignment_collection");
                    for (int i = 0; i < assignments.length(); i++) {
                        JSONObject c = assignments.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("gradebookItemName");
                        String dueTime = c.getString("dueTimeString");
                        String startTime = c.getString("openTimeString");
                        String instructions = c.getString("instructions");
                        String status = c.getString("status");
                        Log.e("ASSINITEMNAME", itemName);

                        //store the variable needed in a hashmap
                        HashMap<String, String> eachAssign = new HashMap<>();
                        eachAssign.put("itemName", itemName);
                        eachAssign.put("dueTime", dueTime);
                        eachAssign.put("startTime", startTime);
                        eachAssign.put("instructions", instructions);
                        eachAssign.put("status", status);
                        asnList.add(eachAssign);
                        Log.i("ASNLIST",asnList.toString());
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
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the assignment lists
            ListAdapter adapter = new SimpleAdapter( Assignment.this, asnList,
                    R.layout.assign_listitem, new String[]{"itemName", "status","startTime",
                    "dueTime"},new int[]{R.id.itemName, R.id.status, R.id.openTimeString,R.id.dueTimeString});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Assignment.this, eachAssign.class);
                    //send the assignment info to each Assign view
                    intent.putExtra("assign info",asnList.get(position));
                    startActivity(intent);
                }
            });

        }

    }
}


