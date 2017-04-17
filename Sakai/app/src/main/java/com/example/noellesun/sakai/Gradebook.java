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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.
        Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Gradebook extends AppCompatActivity {
    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> gradeList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String fixurl = "https://sakai.duke.edu/direct/gradebook/site/";
    String cookiestr;
    String siteid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradebook);
        lv = (ListView) findViewById(R.id.gradebooklist);
        //findViewById(R.id.sites).setOnClickListener(sitesclick);
        siteid = getIntent().getExtras().getString("SiteID");
        Log.i("GRADESiteid:",siteid);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new GetGrade().execute();
    }
    final OnClickListener sitesclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toSites = new Intent(Gradebook.this, sites.class);
            startActivity(toSites);
        }
    };

    private class GetGrade extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Gradebook.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = fixurl + siteid + ".json";
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            Log.e(TAG, "GRADEJSON: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray assignments = jsonObj.getJSONArray("assignments");
                    for (int i = 0; i < assignments.length(); i++) {
                        JSONObject c = assignments.getJSONObject(i);
                        String itemName = c.getString("itemName");
                        String grade = c.getString("grade");
                        String points = c.getString("points");
                        Log.e("ITEMNAME", itemName);

                        HashMap<String, String> eachGrade = new HashMap<>();
                        eachGrade.put("itemName", itemName);
                        eachGrade.put("grade", grade);
                        eachGrade.put("points", points);
                        gradeList.add(eachGrade);
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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.e("postexe","prepare to list");
            Log.i("gradelist",Integer.toString(gradeList.size()));

            /*TextView description1 = (TextView) findViewById(R.id.header_line1);
            TextView description2 = (TextView) findViewById(R.id.header_line2);
            description1.setText("This is the first line describing the list");
            description2.setText("Another description in the header");


            TextView columnHeader1 = (TextView) findViewById(R.id.column_header1);
            TextView columnHeader2 = (TextView) findViewById(R.id.column_header2);

            columnHeader1.setText("Sequence");
            columnHeader2.setText("Precipitation (inches)");
            lv.addHeaderView(columnHeader1);
            lv.addHeaderView(columnHeader2);*/


            ListAdapter adapter = new SimpleAdapter( Gradebook.this, gradeList,
                    R.layout.gradebook_listitem, new String[]{"itemName", "grade",
                    "points"},new int[]{R.id.itemName, R.id.grade,R.id.points});
            lv.setAdapter(adapter);
        }

    }
}
