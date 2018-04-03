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
    private final static int ORDINARY_ACTIVITY_RESULT_CODE = 0;
    private String TAG = eachSite.class.getSimpleName();
    public Button assignments;
    String userid;
    String siteid;
    static String activityLabelclick = "Navigator";
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
                finish();
                return;
            } else {
                userid = ids[0];
                siteid = ids[1];
                activityLabelclick = b.getString("activityLabelclick");
            }
        }


        Log.i("EachSite:",siteid);
        //activityLabelclick =  (String) getIntent().getStringExtra("activityLabelclick");
        setTitle(activityLabelclick);
        setContentView(R.layout.activity_each_site); // Ethan: need modification
        // Ethan: add notification clicker
        findViewById(R.id.Assignments).setOnClickListener(assignclick);
        findViewById(R.id.Resources).setOnClickListener(resouclick);
        findViewById(R.id.Announcements).setOnClickListener(announceclick);
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
            b.putString("activityLabelclick", activityLabelclick);
            toProfile.putExtras(b);
            Log.i(TAG, "profilelclick");
            startActivityForResult(toProfile,ORDINARY_ACTIVITY_RESULT_CODE);
        }
    };
    //redirect to resource view, send the selected siteid
    public final OnClickListener resouclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toResources = new Intent(eachSite.this, Resources.class);
            toResources.putExtra("SiteID",siteid);
            toResources.putExtra("activityLabelclick", activityLabelclick);
            Log.i(TAG, "resouclick");
            startActivityForResult(toResources,ORDINARY_ACTIVITY_RESULT_CODE);
        }
    };

    public final OnClickListener announceclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toAnnouncement = new Intent(eachSite.this, Announcement.class);
            toAnnouncement.putExtra("SiteID",siteid);
            toAnnouncement.putExtra("activityLabelclick", activityLabelclick);
            startActivity(toAnnouncement);
        }
    };

    public final OnClickListener assignclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toAssignments = new Intent(eachSite.this, Assignment.class);
            toAssignments.putExtra("SiteID",siteid);
            toAssignments.putExtra("activityLabelclick", activityLabelclick);
            Log.i(TAG, "assignclick");
            startActivityForResult(toAssignments,ORDINARY_ACTIVITY_RESULT_CODE);
        }
    };
    //redirect to sites view
    final OnClickListener sitesclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    //redirect to gradebook view, send the selected siteid
    final OnClickListener gradebookclick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent toGradebook = new Intent(eachSite.this, Gradebook.class);
            toGradebook.putExtra("SiteID",siteid);
            toGradebook.putExtra("activityLabelclick", activityLabelclick);
            Log.i(TAG, "gradebookclick");
            startActivityForResult(toGradebook,ORDINARY_ACTIVITY_RESULT_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        // check that it is the SecondActivity with an OK result
        if (requestCode == ORDINARY_ACTIVITY_RESULT_CODE) {
            if (resultCode == 1) {
                Log.e(TAG, "你进入了Return");
                finish();
            }
        }
    }
}