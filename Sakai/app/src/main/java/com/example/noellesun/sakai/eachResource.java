package com.example.noellesun.sakai;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

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

public class eachResource extends AppCompatActivity {

    //String instructions;
    String resource_url;
    String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i( "eachResourcestr","in eachresource");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_resource);
        String activityLabel = getIntent().getExtras().getString("activityLabel");
        setTitle(activityLabel);
        //Get selected assignment's info from Assignment view
        HashMap<String, String> info = (HashMap<String, String>)getIntent().getSerializableExtra("resource info");
        if(info != null) {
            String itemName = info.get("itemName");
            String numChildren = info.get("numChildren");
            String createdBy = info.get("createdBy");
            //parse html formatted text into plain text
            //resource_url = Html.fromHtml(info.get("url")).toString();
            String type = info.get("type");
            size = info.get("size");
            resource_url = info.get("resource_url");


            Log.e("In each resource", itemName);
            Log.e("In each resource", numChildren);
            Log.e("In each resource", createdBy);
            Log.e("In each resource", resource_url);
            Log.e("In each resource", type);
            Log.e("In each resource", size);

            Button button = (Button) findViewById(R.id.openPdf);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(eachResource.this, PdfViewer.class);
                    startActivity(intent);
                }
            });
            loadText();
        }
    }

    // deprecation
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    void loadText(){
        TextView url = (TextView) findViewById(R.id.resource_url);
        //url.setAutoLinkMask(Linkify.ALL);
        String new_url = getResources().getString(R.string.res_fir_line);
        new_url += resource_url;
        url.setText(new_url);

        TextView text_size = (TextView) findViewById(R.id.size);
        //url.setAutoLinkMask(Linkify.ALL);
        String new_size = getResources().getString(R.string.res_sec_line);
        new_size += size;
        Log.e("new size", new_size);
        text_size.setText(new_size);

    }


}