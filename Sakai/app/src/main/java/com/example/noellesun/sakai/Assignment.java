package com.example.noellesun.sakai;

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
import java.util.Collections;
import java.util.HashMap;


public class Assignment extends AppBaseActivity {
    private String TAG = Assignment.class.getSimpleName();
    //reviewed Login, sites, added comments and restructured some code
    //private ArrayList<HashMap<String, String>> asnList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
    String fixurl = "https://sakai.duke.edu/direct/assignment/site/";
    String cookiestr;
    String siteid;
    static String activityLabel = "Assignment";
    static String activityLabelclick;
    static ArrayList<ListCellAssign> assignlist = new ArrayList<ListCellAssign>();
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        lv = (ListView) findViewById(R.id.assignlist);
        siteid = getIntent().getExtras().getString("SiteID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/"+ "Assignments";
        Log.i("ASSIGNiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Assignment.GetAssign().execute();
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
            Intent toSites = new Intent(Assignment.this, sites.class);
            startActivity(toSites);
        }
    };

    private ArrayList sortAndAddSections(ArrayList<ListCellAssign> itemList) {
        ArrayList tempList = new ArrayList();
        Collections.sort(itemList, Collections.reverseOrder());
        //Loops thorough the list and add a section header in tempList
        String header = "";
        for(int i = 0; i < itemList.size(); i++) {
            //If it is the start of a new section we create a new listcell and add it to our array
            if(!(header.equals(itemList.get(i).getAssignCategory()))){
                ListCellAssign sectionCell = new ListCellAssign(itemList.get(i).getAssignCategory(), null,null, null, null, null);
                sectionCell.setToAssignHeader();
                tempList.add(sectionCell);
                header = itemList.get(i).getAssignCategory();
            }
            tempList.add(itemList.get(i));
        }
        return tempList;
    }

    // AsuncTask that is used to get json from url
    //@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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
            String url_site = "https://sakai.duke.edu/direct/site/"+ siteid + ".json";
            Log.i("assign_url",url);
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            //String jsonStr1 = sh.makeServiceCall(url_site, cookiestr); // use for class name
            Log.e(TAG, "ASSIGNJSON: " + jsonStr);
            assignlist.clear();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //JSONObject jsonObj1 = new JSONObject(jsonStr1);
                    JSONArray assignments = jsonObj.getJSONArray("assignment_collection");
                    //title = jsonObj1.getString("title") + " / "+ "Assignments";
                    for (int i = 0; i < assignments.length(); i++) {
                        JSONObject c = assignments.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("entityTitle");
                        String dueTime = c.getString("dueTimeString");
                        String startTime = c.getString("openTimeString");
                        String instructions = c.getString("instructions");
                        String status = c.getString("status");
                        String gradeScale = c.getString("gradeScaleMaxPoints");
                        Log.e("ASSINITEMNAME", itemName);
                        //store the variable needed in a hashmap
                        HashMap<String, String> eachAssign = new HashMap<>();
                        eachAssign.put("itemName", itemName);
                        eachAssign.put("dueTime", dueTime);
                        eachAssign.put("startTime", startTime);
                        eachAssign.put("instructions", instructions);
                        eachAssign.put("status", status);
                        assignlist.add(new ListCellAssign(Integer.toString(i), itemName, status, gradeScale, dueTime, eachAssign));
                        //asnList.add(eachAssign);
                        //Log.i("ASNLIST",asnList.toString());
                        //Log.e("list_status", assignlist.get(i).getAssignCategory());
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
            assignlist = sortAndAddSections(assignlist);
            Log.e("background","done!");
            return null;
        }
        //@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(Void result) {
            Log.e("postexe","prepare to list");
            super.onPostExecute(result);
            setTitle(activityLabel);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the assignment lists
            //ListAdapter adapter = new SimpleAdapter( Assignment.this, asnList,
            //        R.layout.assign_listitem, new String[]{"itemName", "status","startTime",
            //        "dueTime"},new int[]{R.id.itemName, R.id.status, R.id.openTimeString,R.id.dueTimeString});
            //lv.setAdapter(adapter);
            ListAdapterAssign adapter = new com.example.noellesun.sakai.ListAdapterAssign(Assignment.this, assignlist);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(Assignment.this, eachAssign.class);
                    ListCellAssign temp_lc = (ListCellAssign) parent.getAdapter().getItem(position);
                    Log.i("position", temp_lc.getAssignId());
                    if(temp_lc.getAssignTitlename() == null) {
                        return;//lv.setClickable(false);
                    }
                    //send the assignment info to each Assign view
                    intent.putExtra("assign info", assignlist.get(position).getEachAssign());
                    //intent.putExtra("assign info",asnList.get(position));
                    intent.putExtra("activityLabelclick", activityLabelclick);
                    startActivity(intent);
                }
            });
        }
    }
}