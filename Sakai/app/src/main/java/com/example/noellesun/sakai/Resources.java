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
import java.util.ArrayList;

import com.example.noellesun.sakai.ListCellRes;
import com.example.noellesun.sakai.ListAdapterRes;
import com.example.noellesun.sakai.ResClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.ListView;

public class Resources extends AppBaseActivity {

    private String TAG = sites.class.getSimpleName();
    private ArrayList<HashMap<String, String>> resList = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView treeview;
//    String fixurl = "https://sakai.duke.edu/access/content/group/";
    String fixurl = "https://sakai.duke.edu/direct/content/site/";
    String cookiestr;
    String siteid;
    String userid;
    static String activityLabel = "Resources";
    static String activityLabelclick;

    static ArrayList<ListCellRes> ResCells = new ArrayList<ListCellRes>();
    static ArrayList<ListCellRes> ResCellsData = new ArrayList<ListCellRes>();
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_resources);
//        //treeview = (ListView) findViewById(R.id.resourcelist);
//        siteid = getIntent().getExtras().getString("SiteID");
//        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
//        activityLabel = activityLabelclick + "/" + "Resources";
//        Log.i("RESOURiteid:",siteid);
//        //set cookies in order to maintain the same session
//        final CookieManager cookieManager = CookieManager.getInstance();
//        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
//        new Resources.GetResour().execute();

        //});
        /********/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        treeview = (ListView) findViewById(R.id.resourcelist);


        siteid = getIntent().getExtras().getString("SiteID");
        userid = getIntent().getExtras().getString("USERID");
        activityLabelclick = (String)getIntent().getExtras().getString("activityLabelclick");
        activityLabel = activityLabelclick + "/" + "Resources";
        Log.i("RESOURiteid:",siteid);
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        new Resources.GetResour().execute();



        //init();

        //ListView treeview = (ListView) findViewById(R.id.treeview);
//        TreeViewAdapter treeViewAdapter = new TreeViewAdapter(
//                elements, elementsData, inflater);
//        TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
//        treeview.setAdapter(treeViewAdapter);
//        treeview.setOnItemClickListener(treeViewItemClickListener);


        /********/
        establish_nav(siteid,userid, activityLabelclick);
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
    // AsyncTask that is used to get json from url
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
            ResCellsData.clear();
            ResCells.clear();
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray resources = jsonObj.getJSONArray("content_collection");
                    //title = resources.getJSONObject(0).getString("entityTitle") + " / " + "Resources";
                    for (int i = 0; i < resources.length(); i++) {
                        JSONObject c = resources.getJSONObject(i);
                        //get variable needed from JSON object
                        String id = Integer.toString(i);
                        String itemName = c.getString("entityTitle");
                        String numChildren = c.getString("numChildren");
                        String createdBy = c.getString("author");
                        String resource_url = c.getString("url");
                        String type = c.getString("type");
                        String size = "";
                        //repositories don't have size, files have size
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
                        ListCellRes ResCell= new ListCellRes(id, itemName,numChildren,createdBy, resource_url, type, size, eachResource);
                        ResCellsData.add(ResCell);
                        Log.i("RESLIST", resList.toString());
                    }


                    ResCellsData = getResCellsData(ResCellsData);
                    ResCells = getResCells(ResCells, ResCellsData);

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

        public ArrayList<ListCellRes> getResCellsData (ArrayList<ListCellRes> ResCellsData){
            int result = traverse(ResCellsData, 0);
            int i = 0;
            for(; i < ResCellsData.size(); i++){
                if(ResCellsData.get(i).getLevel() == 1){
                    ResCellsData.get(i).setExpanded(true);
                }
                else{
                    ResCellsData.get(i).setExpanded(false);
                }
            }
            return ResCellsData;
        }

        public int traverse (ArrayList<ListCellRes> ResCellsData, int cur){
            int c = Integer.parseInt(ResCellsData.get(cur).getResnumChildren());
            int next = cur + 1;
            if(c == 0){
                ResCellsData.get(cur).setHasChildren(false);
            }
            else{
                ResCellsData.get(cur).setHasChildren(true);
            }
            while(c != 0){
                if(next >= ResCellsData.size()) break;
                ResCellsData.get(next).setpid(cur);
                ResCellsData.get(next).setlevel(ResCellsData.get(cur).getLevel()+1);
                next = traverse(ResCellsData, next);
                --c;
            }
            return next;
        }

        public ArrayList<ListCellRes> getResCells (ArrayList<ListCellRes> ResCells ,ArrayList<ListCellRes> ResCellsData){
            int i = 0;
            for(; i < ResCellsData.size(); i++){
                if(ResCellsData.get(i).getLevel() == 1){
                    ResCells.add(ResCellsData.get(i));

                }
            }
            return ResCells;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            setTitle(activityLabel);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //parse data into the resources lists
//            ListAdapterRes adapter = new ListAdapterRes (ResCells, ResCellsData, inflater);
////            treeview.setAdapter(adapter);
////            ResClickListener treeViewItemClickListener = new ResClickListener(treeViewAdapter);
//
//
//
////            final ListAdapter adapter = new ListAdapterRes(Resources.this, resList,
////                    R.layout.resource_listitem, new String[]{"itemName", "size",
////                    "createdBy"}, new int[]{R.id.itemName, R.id.size, R.id.createdBy});
//            treeview.setAdapter(adapter);
//
//            //ListView treeview = (ListView) findViewById(R.id.treeview);
            final ListAdapterRes adapter = new ListAdapterRes(
                    ResCells, ResCellsData, inflater);
            //ResClickListener resClickListener = new ResClickListener(adapter);
            treeview.setAdapter(adapter);
            //treeview.setOnItemClickListener(resClickListener);



            treeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//
//                    Intent intent = new Intent(Resources.this, eachResource.class);
//                    //send the resource info to each Resource view
//                    intent.putExtra("resource info", resList.get(position));
//                    intent.putExtra("activityLabelclick", activityLabelclick);
//                    //intent.putExtra("resource info", resList);
//                    startActivity(intent);
//
//
//
//                }

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    //点击的item代表的元素
                    ListCellRes element = (ListCellRes) adapter.getItem(position);
                    //树中的元素
                    ArrayList<ListCellRes> elements = adapter.getResCells();
                    Log.i("size " , Integer.toString(elements.size()));
                    //ArrayList<ListCellRes> all_elements = elements;
                    //元素的数据源
                    //Log.i("elements " , elements.toString());
                    ArrayList<ListCellRes> elementsData = adapter.getResCellsData();
                    //Log.i("elementsData " , elementsData.toString());
                    //点击没有子项的item直接返回
                    if (!element.isHasChildren()) {

                        //return;
                        Intent intent = new Intent(Resources.this, eachResource.class);
                        //intent.setClassName("com.example.noellesun.sakai.ResClickListener","com.example.noellesun.sakai.eachResource");
                        //intent.setClass(this, eachResource.class);
                        //Intent intent = new Intent(ListAdapterRes.this, eachResource.class);
                        //send the resource info to each Resource view
                        intent.putExtra("resource info", element.getEachRes());
                        intent.putExtra("activityLabelclick", activityLabelclick);
                        //intent.putExtra("resource info", resList);
                        startActivity(intent);

                    }

                    if (element.isExpanded()) {
                        element.setExpanded(false);
                        Log.i("fold " , Integer.toString(element.getResId()));
                        //删除节点内部对应子节点数据，包括子节点的子节点...
                        ArrayList<ListCellRes> elementsToDel = new ArrayList<ListCellRes>();
                        for (int i = position + 1; i < elements.size(); i++) {
                            if (element.getLevel() >= elements.get(i).getLevel())
                                break;
                            elementsToDel.add(elements.get(i));
                        }
                        elements.removeAll(elementsToDel);
                        adapter.notifyDataSetChanged();
                    } else {
                        element.setExpanded(true);
                        //Log.i("Expand " , Integer.toString(element.getResId()));
                        //从数据源中提取子节点数据添加进树，注意这里只是添加了下一级子节点，为了简化逻辑
                        int i = 1;//注意这里的计数器放在for外面才能保证计数有效
                        for (ListCellRes e : elementsData) {
                            Log.i("Find id " , Integer.toString(e.getResId()));
                            Log.i("Find pid " , Integer.toString(e.getPid()));
                            if (e.getPid() == element.getResId()) {
                                e.setExpanded(false);
                                Log.i("Add " , e.toString());
                                elements.add(position + i, e);
                                //elements.add(e);
                                Log.i("Add " , Integer.toString(e.getResId()));
                                i ++;
                            }
                        }
                        //Log.i("size " , Integer.toString(elements.size()));
//                        for(; i < all_elements.size(); i++){
//                            ListCellRes e = adapter.getResCellsData().get(i);
//                            Log.i("Find id " , Integer.toString(e.getResId()));
//                            Log.i("Find pid " , Integer.toString(e.getPid()));
//                            if (e.getPid() == element.getResId()) {
//                                e.setExpanded(false);
//                                Log.i("Add " , e.toString());
//                                elements.add(position + i, e);
//                                //Log.i("Add " , Integer.toString(element.getResId()));
//                                //i ++;
//                            }
//                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }
}
