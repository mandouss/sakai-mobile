package com.example.noellesun.sakai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class eachAnnounce extends AppCompatActivity {
    //String instructions;
    String resource_url;
    String instructions;
    static String itemName, modifiedTimeString, createdBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAnnounceinstr", "in eachannounce");
        super.onCreate(savedInstanceState);
        String activityLabel = getIntent().getExtras().getString("activityLabel");
        setContentView(R.layout.activity_each_announce);
        setTitle(activityLabel);

        //Get selected Announcement's info from Announcement view
        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("announce info");

        if (info != null) {
            itemName = info.get("itemName");
            modifiedTimeString = info.get("modifiedTimeString");
            String millisecTimeString = info.get("millisecTimeString");
            createdBy = info.get("createdBy");
            //parse html formatted text into plain text
            instructions = Html.fromHtml(info.get("instructions")).toString();
            resource_url = info.get("resource_url");
            // eachAnnounce.put("title", activityLabel);
            setTitle(itemName);

            Log.i("In each Announce", itemName);
            Log.i("In each Announce", modifiedTimeString);
            Log.e("In each Announce", millisecTimeString);
            Log.e("In each Announce", createdBy);
            Log.e("In each Announce", instructions);
            Log.e("In each Announce", resource_url);

            loadText();
            // setTitle(activityLabel);
        }
    }

    @SuppressWarnings("deprecation")
    /*
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    */

    void loadText(){
        // TextView instruct = (TextView) findViewById(R.id.instruction);
        // instruct.setText(instructions);

        TextView titleName = (TextView) findViewById(R.id.announcetitle);
        TextView instruct = (TextView) findViewById(R.id.instruction);
        titleName.setText(itemName);
        instruct.setText(instructions);
    }
}
