package com.example.noellesun.sakai;

import java.util.ArrayList;
import java.util.Collections;
import android.app.Activity;

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
    //use static userid and sitesids to keep the original userid and sitesids
    static  String userid; // Ethan: why do we need static
    static ArrayList<String> sitesids;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String fixurl = "https://sakai.duke.edu/direct/site/";
    String cookiestr;
    //    static ArrayList<LinkedHashMap<String, site_info>> sitetitleist = new ArrayList<>();
    //static ArrayList<HashMap<String, String>> sitetitleist = new ArrayList<>();
    static ArrayList<ListCell> sitelist = new ArrayList<ListCell>();
    static ArrayList<String> idarray = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);
        lv = (ListView) findViewById(R.id.list);
        Log.i("siteList",Integer.toString(sitelist.size()));

        findViewById(R.id.lo).setOnClickListener(logout);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Log.i(TAG,cookiestr);
        // from login activity
        if(getIntent().getExtras().getString("ID").equals("Login")){
            Bundle b = getIntent().getExtras();
            Log.i("Sites:", "got intent");
            idarray = b.getStringArrayList("ID_ARRAY");
            userid = idarray.get(0);
            idarray.remove(0);
            sitesids = idarray;
            Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_LONG).show();
            Log.i("Here:", "I am here!");
            new GetSites().execute();
            //sort site by term;
        }
        else{  // Ethan: do not need? If we need it we can write abstraction to reuse code
            Log.i("redirect","From other activities!");
            Log.i("Sites:", "now in sites create");
            ListAdapter adapter = new com.example.noellesun.sakai.ListAdapter(sites.this, sitelist);
            lv.setAdapter(adapter);
            //set click event
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sites.this, eachSite.class);
                    //get the position of one click
                    ListCell temp_lc = (ListCell)parent.getAdapter().getItem(position);
                    //!!! check if it is header, dynamic action to be complete
                    if(temp_lc.getTitlename() == null) {
                        return;//lv.setClickable(false);
                    }
                    Log.i("position is", temp_lc.getId());
                    String [] ids = {userid, temp_lc.getId()};
                    Bundle b = new Bundle();
                    b.putStringArray("IDS",ids);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }

    }
    final OnClickListener logout = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();//close all activities
            System.exit(0);
//            Intent intent = new Intent(getBaseContext(), Login.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//close all activities
//            startActivity(intent);
        }
    };
    private ArrayList sortAndAddSections(ArrayList<ListCell> itemList) {
        ArrayList tempList = new ArrayList();
        Collections.sort(itemList);
        //Loops thorough the list and add a section header in tempList
        String header = "";
        for(int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!(header.equals(itemList.get(i).getCategory()))){
                ListCell sectionCell = new ListCell(itemList.get(i).getCategory(), null,null, null);
                sectionCell.setToSectionHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getCategory();
            }
            tempList.add(itemList.get(i));
        }
        return tempList;
    }



    private class GetSites extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(sites.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            Log.i("show", "message");
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            //receive userId and siteIs from Login view
            HttpHandler sh = new HttpHandler();
            for (int i = 0; i < sitesids.size(); i++) {
                String siteurl = fixurl + sitesids.get(i) + ".json";
                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall(siteurl, cookiestr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        // Getting JSON Array node and //parse json data into the sitelist
                        String category = jsonObj.getString("type");
                        if(!category.equals("course")) {
                            category = "Project";
                        }
                        else{
                            category = jsonObj.getJSONObject("props").getString("term");
                        }
                        String instructor = jsonObj.getJSONObject("siteOwner").getString("userDisplayName");
                        String titleName = jsonObj.getString("title");
                        Log.e("titleName",titleName);
                        sitelist.add(new ListCell(sitesids.get(i), titleName, category, instructor));
                        Log.e("after_sitessize",Integer.toString(sitelist.size()));
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
            //sort site by term;
            sitelist = sortAndAddSections(sitelist);
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
            //sitelist = sortAndAddSections(sitelist);
            ListAdapter adapter = new com.example.noellesun.sakai.ListAdapter(sites.this, sitelist);
            //parse json data into the sites list

            lv.setAdapter(adapter);

            //set click event
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sites.this, eachSite.class);
                    ListCell temp_lc = (ListCell)parent.getAdapter().getItem(position);
                    String [] ids = {userid, temp_lc.getId(),"0"};
                    Log.i("position", temp_lc.getId());
                    if(temp_lc.getTitlename() == null) {
                        return;//lv.setClickable(false);
                    }
                    Bundle b = new Bundle();
                    b.putStringArray("IDS",ids);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
    }
}
