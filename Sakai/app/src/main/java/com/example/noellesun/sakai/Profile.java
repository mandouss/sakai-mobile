package com.example.noellesun.sakai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends AppCompatActivity {
    String userid;
    String fixurl = "https://sakai.duke.edu/direct/profile/";
    private String TAG = Login.class.getSimpleName();
    String cookiestr = "";
    private ProgressDialog pDialog;
    String jsonname;
    String jsonemail;
    String jsonnickname;
    String jsondegree;
    TextView name,email, nickname,degree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Log.e("PROFILE",cookiestr);
        Bundle b = getIntent().getExtras();
        findViewById(R.id.lo).setOnClickListener(logout);

        userid = b.getString("USERID");
        Log.e("PROFILE!!!!", userid);
        new GetProfile().execute();

        //findViewById(R.id.sitesbtn).setOnClickListener(sitesclick);

    }


    final View.OnClickListener logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();//close all activities
            finishAffinity();
            System.exit(0);

        }
    };

//    void loadText(){
//        Log.i("name!!!!!!!!",jsonname);
//        Log.i("name!!!!!!!!",jsonemail);
//        Log.i("name!!!!!!!!",jsonnickname);
//        Log.i("name!!!!!!!!",jsondegree);
//    }
    private class GetProfile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Showing progress dialog
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
//            if (pDialog.isShowing())
//                pDialog.dismiss();
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String url = fixurl + userid.toString() + ".json";
            String imageurl = fixurl + userid.toString() + "/image";
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            //Log.i("jsonStr!!!!!!!!",jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    jsonname = jsonObj.getString("displayName");
                    jsonemail = jsonObj.getString("email");
                    jsonnickname = jsonObj.getString("nickname");
                    jsondegree = jsonObj.getString("course");

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
            Log.i("profileimage","prepare to go intry");
            try {

                URL imageURL = new URL(imageurl);
                HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
                connection.setRequestProperty("Cookie", cookiestr);
                connection.setDoInput(true);
                connection.connect();
                Log.i("profileimage",imageurl);
                InputStream input = connection.getInputStream();
                Log.i("profileimage","after get");
                final Bitmap myBitmap = BitmapFactory.decodeStream(input);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("profileimage to show","");
                        ImageView myImage = (ImageView) findViewById(R.id.imageView);
                        myImage.setImageBitmap(myBitmap);
                    }
                });

            } catch (IOException e) {
                Log.i("profileimage","ioexception");
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            name = (TextView) findViewById(R.id.nameview);
            email = (TextView) findViewById(R.id.emailview);
            nickname = (TextView) findViewById(R.id.nicknameview);
            degree = (TextView) findViewById(R.id.degreeview);

            name.setText(jsonname);
            email.setText(jsonemail);
            nickname.setText(jsonnickname);
            degree.setText(jsondegree);
            Log.e("postexe", "prepare to list");//not execute this!!???
            super.onPostExecute(result);
             //Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }


    }
}
