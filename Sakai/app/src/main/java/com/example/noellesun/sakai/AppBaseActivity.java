package com.example.noellesun.sakai;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AppBaseActivity extends AppCompatActivity{
    private FrameLayout view_stub; //This is the framelayout to keep your content view
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String siteID;
    private String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("AppBaseActivity", "onCreate");
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.app_base_layout);// The base layout that contains your navigation drawer.
        view_stub = (FrameLayout) findViewById(R.id.view_stub);
        NavigationView navigation_view = (NavigationView) findViewById(R.id.navi_id);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_button_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities

    protected void establish_nav(final String siteid, final String activityLabelclick){
        siteID = siteid;
        NavigationView n = (NavigationView)findViewById(R.id.navi_id);
        n.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(false);
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        if(item.getTitle().equals("Assignments")){
                            Intent toAssignment = new Intent(getBaseContext(), Assignment.class);
                            toAssignment.putExtra("SiteID",siteid);
                            toAssignment.putExtra("activityLabelclick",activityLabelclick);
                            startActivity(toAssignment);
                        }else if(item.getTitle().equals("Gradebook")) {
                            Intent toSites = new Intent(getBaseContext(), Gradebook.class);
                            toSites.putExtra("SiteID",siteid);
                            toSites.putExtra("activityLabelclick",activityLabelclick);
                            startActivity(toSites);
                        }else if(item.getTitle().equals("Resources")) {
                            Intent toSites = new Intent(getBaseContext(), Resources.class);
                            toSites.putExtra("SiteID",siteid);
                            toSites.putExtra("activityLabelclick",activityLabelclick);
                            startActivity(toSites);
                        }else if(item.getTitle().equals("Other Courses")) {
                            Intent intent = new Intent();//getBaseContext(), sites.class
                            intent.putExtra("Result", "1");
                            intent.putExtra("activityLabelclick", activityLabelclick);
                            setResult(1, intent);
                        }else if(item.getTitle().equals("Announcements")){
                            Intent intent = new Intent(getBaseContext(), Announcement.class);
                            intent.putExtra("SiteID", siteid);
                            intent.putExtra("activityLabelclick", activityLabelclick);
                            startActivity(intent);
                        }else if(item.getTitle().equals("Lesson")){
                            Intent intent = new Intent(getBaseContext(), Lesson.class);
                            Bundle b = new Bundle();
                            b.putString("siteId", siteid);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                        finish();
                        return true;
                    }
                }
        );
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Override all setContentView methods to put the content view to the FrameLayout view_stub
     * so that, we can make other activity implementations looks like normal activity subclasses.
     */
    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }
    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
            Toast.makeText(getApplicationContext(),"profile clicked",Toast.LENGTH_SHORT).show();
//            Intent toProfile = new Intent(eachSite.this, Profile.class);
//            Bundle b = new Bundle();
//            b.putString("USERID", userid);
//            toProfile.putExtras(b);
//            Log.i(TAG, "profilelclick");
//            startActivityForResult(toProfile,ORDINARY_ACTIVITY_RESULT_CODE);
        }

        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.item1:
//                // handle it
//                break;
//            case R.id.item2:
//                // do whatever
//                break;
//            // and so on...
//        }
//        return false;
//    }
}

