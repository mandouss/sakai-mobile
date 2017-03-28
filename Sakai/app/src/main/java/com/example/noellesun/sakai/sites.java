package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class sites extends AppCompatActivity {
    private String TAG = sites.class.getSimpleName();
    String userid;
    ArrayList<String> sitesids;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String fixurl = "https://sakai.duke.edu/direct/site/";
    String cookiestr;
    ArrayList<HashMap<String, String>> sitetitleist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);
        lv = (ListView) findViewById(R.id.list);
        //lv.setClickable(true);
        //lv.setOnClickListener(listListener);

        sitetitleist = new ArrayList<>();

        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Log.e("Sites:", "now in sites create");

        Bundle b = getIntent().getExtras();
        Log.e("Sites:", "got intent");
        ArrayList<String> idarray = b.getStringArrayList("ID_ARRAY");
        //Log.e("Sites:", idarray.toString());
        userid = idarray.get(0);
        idarray.remove(0);
        sitesids = idarray;
        Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_LONG).show();
        Log.e("aftergetid:", "I am here!");

        new GetSites().execute();
    }
   /* public OnClickListener OnItemClickListener = new OnClickListener() {
        public void onClick(View v) {
            //code to be written to handle the click event
        }
    };
    final OnClickListener sitesclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toSites = new Intent(sites.this, eachSite.class);
            startActivity(toSites);
        }
    };*/

    private class GetSites extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(sites.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //receive userId and siteIs from Login view


            HttpHandler sh = new HttpHandler();
            for (int i = 0; i < sitesids.size(); i++) {
                String siteurl = fixurl + sitesids.get(i) + ".json";
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(siteurl, cookiestr);
                //Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        // Getting JSON Array node
                        String titlename = jsonObj.getString("title");
                        Log.e("titlename",titlename);
                        // tmp hash map for single sitetitle
                        HashMap<String, String> sitetitle = new HashMap<>();
                        sitetitle.put("title", titlename);
                        sitetitleist.add(sitetitle);
                        Log.e("after_sitessize",Integer.toString(sitetitleist.size()));
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
            }
            Log.e("background","done!");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.e("postexe","prepare to list");//not execute this!!???
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            Log.e("postexe","prepare to list");
            ListAdapter adapter = new SimpleAdapter( sites.this, sitetitleist,
                    R.layout.list_item, new String[]{"title"}, new int[]{R.id.title});

            lv.setAdapter(adapter);
            //set click event
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sites.this, eachSite.class);
                    //String message = "abc";
                    //intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                }
            });
            //findViewById(R.id.sites).setOnClickListener(sitesclick);
        }

    }
}