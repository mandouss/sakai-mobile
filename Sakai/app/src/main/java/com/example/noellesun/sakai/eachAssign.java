package com.example.noellesun.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class eachAssign extends AppCompatActivity {
    String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("eachAssigninstr","in eachassign");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_assign);

        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("assign info");
        String itemName = info.get("itemName");
        String dueTime = info.get("dueTime");
        String startTime = info.get("startTime");
        instructions = Html.fromHtml(info.get("instructions")).toString();
        String status = info.get("status");
        loadText();
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
        /*TextView name = (TextView) findViewById(R.id.name);
        name.setText(jsonname);*/
        /*TextView email = (TextView) findViewById(R.id.emailview);
        email.setText(jsonemail);
        TextView nickname = (TextView) findViewById(R.id.nicknameview);
        nickname.setText(jsonnickname);*/
        TextView instruct = (TextView) findViewById(R.id.instruction);
        instruct.setText(instructions);
    }
}
