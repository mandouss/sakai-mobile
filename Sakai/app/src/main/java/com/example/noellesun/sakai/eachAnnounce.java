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
    static String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAnnounceinstr", "in eachannounce");
        super.onCreate(savedInstanceState);
        //String activityLabel = getIntent().getExtras().getString("activityLabel");
        setContentView(R.layout.activity_each_announce);
        String activityLabel = getIntent().getExtras().getString("activityLabelclick") + "/" + "Announcements";
        setTitle(activityLabel);

        //Get selected Announcement's info from Announcement view
        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("Announce info");

        if (info != null) {
            itemName = info.get("itemName").toString();
            String modifiedTimeString = info.get("modifiedTimeString");
            String millisecTimeString = info.get("millisecTimeString");
            String createdBy = info.get("createdBy");
            //parse html formatted text into plain text
            instructions = Html.fromHtml(info.get("instructions")).toString();
            resource_url = info.get("resource_url");
            // eachAnnounce.put("title", activityLabel);

            Log.i("In each Announce", itemName);
            Log.i("In each Announce", modifiedTimeString);
            Log.i("In each Announce", millisecTimeString);
            Log.i("In each Announce", createdBy);
            Log.i("In each Announce", instructions);
            Log.i("In each Announce", resource_url);

            // setTitle(itemName);
            loadText();
            // setTitle(activityLabel);
        }
        //else {
            // test whether its null
          //  Log.i("Info list is empty", itemName);
        //}
    }

    @SuppressWarnings("deprecation")
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

        TextView titleName = (TextView) findViewById(R.id.anno_title);
        String new_title = getResources().getString(R.string.anno_fir_line);
        new_title += itemName;
        titleName.setText(new_title);
        Log.e("new itemName", itemName);

        TextView instruct = (TextView) findViewById(R.id.instruction);
        String new_instruct = getResources().getString(R.string.anno_sec_line);
        new_instruct += instructions;
        Log.e("new instructions", instructions);
        instruct.setText(new_instruct);
    }
}
