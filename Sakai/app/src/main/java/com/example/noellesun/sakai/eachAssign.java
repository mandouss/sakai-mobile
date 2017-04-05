package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.HashMap;

public class eachAssign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAssigninstr","in eachassign");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_assign);

        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("assign info");
        String itemName = info.get("itemName");
        String dueTime = info.get("dueTime");
        String startTime = info.get("startTime");
        String instructions = Html.fromHtml(info.get("instructions")).toString();
        String status = info.get("status");
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
}
