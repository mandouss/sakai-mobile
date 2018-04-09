package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class eachAssign extends AppCompatActivity {
    static String itemName, dueTime, startTime;
    String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAssigninstr","in eachassign");
        super.onCreate(savedInstanceState);
        String activityLabel = getIntent().getExtras().getString("activityLabelclick") + "/" + "Assignments";
        setTitle(activityLabel);
        setContentView(R.layout.activity_each_assign);
        //Get selected assignment's info from Assignment view
        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("assign info");
        itemName = info.get("itemName").toString();
        dueTime = info.get("dueTime").toString();
        startTime = info.get("startTime").toString();
        //parse html formatted text into plain text
        instructions = Html.fromHtml(info.get("instructions")).toString();
        String status = info.get("status");
        String title = info.get("title"); //site_title/Assignment
        setTitle(title);
        loadText();
    }
//    @SuppressWarnings("deprecation")
//    public static Spanned fromHtml(String html){
//        Spanned result;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            result = Html.fromHtml(html);
//        }
//        return result;
//    }
    void loadText(){
        TextView titleName = (TextView) findViewById(R.id.assign_itemName);
        TextView instruct = (TextView) findViewById(R.id.instruction);
        titleName.setText(itemName);
        instruct.setText(instructions);
    }
}
