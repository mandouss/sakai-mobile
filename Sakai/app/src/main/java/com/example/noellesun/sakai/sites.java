package com.example.noellesun.sakai;

import android.app.ExpandableListActivity;
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
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class sites extends AppCompatActivity {
    private String TAG = sites.class.getSimpleName();
    //use static userid and sitesids to keep the original userid and sitesids
    static  String userid;
    static ArrayList<String> sitesids;
    private ProgressDialog pDialog;
    //private ListView lv;
    private static String fixurl = "https://sakai.duke.edu/direct/site/";
    String cookiestr;
    //static ArrayList<HashMap<String, String>> sitetitleist = new ArrayList<>(); ;
    static ArrayList<String> idarray = new ArrayList<>();
    // termlist includes both terms and courses
    static HashMap<String, ArrayList<String>> courses = new HashMap<>();
    // term only includes the terms and used for first degree categories
    static ArrayList<String> term = new ArrayList<>();

    private ExpandableListView explv;
    private ExpSiteAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sites);
        //lv = (ListView) findViewById(R.id.list);
        explv = (ExpandableListView) findViewById(R.id.list);

        Log.e("sitetitlelist",Integer.toString(sitetitleist.size()));
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Log.e(TAG,cookiestr);

        if(getIntent().getExtras().getString("ID").equals("Login")){
            Bundle b = getIntent().getExtras();
            Log.e("Sites:", "got intent");
            idarray = b.getStringArrayList("ID_ARRAY");
            userid = idarray.get(0);
            idarray.remove(0);
            sitesids = idarray;
            Toast.makeText(getApplicationContext(), userid, Toast.LENGTH_LONG).show();
            Log.e("aftergetid:", "I am here!");
            new GetSites().execute();
        }
        else{
            Log.e("redirect","From other activities!");
            Log.e("Sites:", "now in sites create");
            //ListAdapter adapter = new SimpleAdapter( sites.this, sitetitleist,
                  // R.layout.list_item, new String[]{"title"}, new int[]{R.id.title});
            adapter = new ExpSiteAdapter(this, termlist )

            lv.setAdapter(adapter);
            //set click event
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sites.this, eachSite.class);
                    Log.e("position is", sitesids.get(position));
                    //send the selected site's id to eachSite view
                    String [] ids = {userid, sitesids.get(position)};
                    Bundle b = new Bundle();
                    b.putStringArray("IDS",ids);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }

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
                        //first add terms to the Arraylist term
                        String type = jsonObj.getString("type");
                        String strterm;
                        if(!type.equals("course")){
                            // if the site is a project instead of a course
                            // use type as the first categories
                            strterm = type;
                            term.add(type);
                        }
                        else{
                            JSONObject props = jsonObj.getJSONObject("props");
                            strterm =props.getString("term");
                            term.add(strterm);
                        }
                        Log.e("term",strterm);
                        // get secondary categories
                        //String titlename = jsonObj.getString("title");
                        //Log.e("titlename",titlename);
                        String coursename = jsonObj.getString("title");
                        Log.e("coursename",coursename );
                        // tmp hash map for single sitetitle
                        //HashMap<String, String> sitetitle = new HashMap<>();
                        //sitetitle.put("title", titlename);
                        //sitetitleist.add(sitetitle);
                        //Log.e("after_sitessize",Integer.toString(sitetitleist.size()));
                        if (courses == null){
                            ArrayList<String> temp_course = new ArrayList<>();
                            temp_course.add(coursename);
                            courses.put(strterm,temp_course);
                        }
                        else{
                            ArrayList<String> temp_course;
                            if(courses.containsKey(strterm)){
                                temp_course = courses.get(strterm);
                                temp_course.add(coursename);
                            }
                            else {
                                temp_course = new ArrayList<>();
                                temp_course.add(coursename);

                            }
                            courses.put(strterm,temp_course);
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
            }

//            print hashmap
            for (HashMap.Entry<String, ArrayList<String> > entry : termlist.entrySet())
            {
//                String result;
//                for(String item : e)
                Log.e("print hashmap", entry.getKey() + "/" + entry.getValue().toString());
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
            //parse json data into the sites list
            ListAdapter adapter = new SimpleAdapter( sites.this, sitetitleist,
                    R.layout.list_item, new String[]{"title"}, new int[]{R.id.title});

            lv.setAdapter(adapter);
            //set click event
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(sites.this, eachSite.class);
                    String [] ids = {userid, sitesids.get(position)};
                    Bundle b = new Bundle();
                    b.putStringArray("IDS",ids);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
    }
}