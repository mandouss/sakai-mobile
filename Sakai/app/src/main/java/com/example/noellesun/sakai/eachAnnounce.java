package com.example.noellesun.sakai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class eachAnnounce extends AppCompatActivity {
    String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAnnounceinstr","in eachannounce");
        super.onCreate(savedInstanceState);
        String activityLabel = getIntent().getExtras().getString("activityLabel");
        setContentView(R.layout.activity_each_announce);
        //Get selected assignment's info from Assignment view
        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("announce info");
        String itemName = info.get("title");
        String modifiedTimeString = info.get("modifiedTimeString");
        String createdBy = info.get("createdBy");
        //parse html formatted text into plain text
        instructions = Html.fromHtml(info.get("instructions")).toString();
        loadText();
        setTitle(activityLabel);
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
        TextView instruct = (TextView) findViewById(R.id.instruction);
        instruct.setText(instructions);
    }
}
