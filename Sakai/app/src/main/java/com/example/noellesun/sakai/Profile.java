package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {
    String userid;
    String fixurl = "https://sakai.duke.edu/direct/profile/";
    private String TAG = Login.class.getSimpleName();
    String cookiestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Bundle b = getIntent().getExtras();
        userid = b.getString("USERID");
        Log.e("PROFILE", userid);


        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String url = fixurl + userid.toString() + ".json";
        Log.e("jsonurl!",url);
        Log.e("profileCookiestr",cookiestr);
        String jsonStr = sh.makeServiceCall(url, cookiestr);

        Log.e("profilejson", "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                TextView name = (TextView) findViewById(R.id.nameview);
                Log.e("TextView",name.toString());
                name.setText(jsonObj.getString("displayname"));
                TextView email = (TextView) findViewById(R.id.emailview);
                name.setText(jsonObj.getString("email"));

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }


    }
}
