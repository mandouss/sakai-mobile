package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class eachAssign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAssigninstr","in eachassign");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_assign);

        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("assign info");
        String itemName = info.get("gradebookItemName");
        String dueTime = info.get("dueTimeString");
        String startTime = info.get("openTimeString");
        String instructions = info.get("instructions");
        String status = info.get("status");
        Log.i("eachAssigninstr",instructions);
    }
}
