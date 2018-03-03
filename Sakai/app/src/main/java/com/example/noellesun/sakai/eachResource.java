package com.example.noellesun.sakai;

/**
 * Created by dearleiii on 3/3/18.
 */
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class eachResource extends AppCompatActivity {

    String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i( "eachResourcestr","in eachresource");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_resource);
        //Get selected assignment's info from Assignment view
        HashMap<String, String> info = (HashMap<String, String>) getIntent().getSerializableExtra("resource info");
        String itemName = info.get("itemName");
        String modifiedTime = info.get("modifiedTime");
        String createdBy = info.get("createdBy");
        //parse html formatted text into plain text
        instructions = Html.fromHtml(info.get("instructions")).toString();
        String access = info.get("access");
        String size = info.get("size");
        loadText();
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
        TextView instruct = (TextView) findViewById(R.id.instruction);
        instruct.setText(instructions);
    }
}
