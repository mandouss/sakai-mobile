package com.example.noellesun.sakai;

import java.util.ArrayList;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.HashMap;

public class eachSite extends AppCompatActivity {
    private String TAG = eachSite.class.getSimpleName();
    public Button assignments;
    String userid;
    String siteid;
    static String title = "Navigator";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the selected site's userid and siteid from sites view
        Bundle b = getIntent().getExtras();
        Log.e("EachSite:", "got intent");
        if (b == null) {
            Log.e(TAG, "can't get bundle");
        } else {
            String[] ids = b.getStringArray("IDS");
            if (ids == null) {
                Log.e(TAG, "ids doesn't contains IDS");
            } else {
                userid = ids[0];
                siteid = ids[1];
            }
        }


        Log.i("EachSite:",siteid);
        title =  (String) getIntent().getStringExtra("site info");
        setTitle(title);
        setContentView(R.layout.activity_each_site); // Ethan: need modification
        // Ethan: add notification clicker
        findViewById(R.id.Assignments).setOnClickListener(assignclick);
        findViewById(R.id.Resources).setOnClickListener(resouclick);
        findViewById(R.id.Gradebook).setOnClickListener(gradebookclick);
        findViewById(R.id.sitesbtn).setOnClickListener(sitesclick);
        findViewById(R.id.profilebtn).setOnClickListener(profilelclick);
    }
    //redirect to profile view, send userid
    final OnClickListener profilelclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toProfile = new Intent(eachSite.this, Profile.class);
            Bundle b = new Bundle();
            b.putString("USERID", userid);
            toProfile.putExtras(b);
            Log.i(TAG, "profilelclick");
            startActivity(toProfile);
        }
    };
    //redirect to resource view, send the selected siteid
    public final OnClickListener resouclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toResources = new Intent(eachSite.this, Resources.class);
            toResources.putExtra("SiteID",siteid);
            Log.i(TAG, "resouclick");
            startActivity(toResources);
        }
    };
    public final OnClickListener assignclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toAssignments = new Intent(eachSite.this, Assignment.class);
            toAssignments.putExtra("SiteID",siteid);
            Log.i(TAG, "assignclick");
            startActivity(toAssignments);
        }
    };
    //redirect to sites view
    final OnClickListener sitesclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toSites = new Intent(eachSite.this, sites.class);
            toSites.putExtra("ID","sitesclick");
            Log.i(TAG, "sitesclick");
            startActivity(toSites);
        }
    };
    //redirect to gradebook view, send the selected siteid
    final OnClickListener gradebookclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toGradebook = new Intent(eachSite.this, Gradebook.class);
            toGradebook.putExtra("SiteID",siteid);
            Log.i(TAG, "gradebookclick");
            startActivity(toGradebook);
        }
    };

}
