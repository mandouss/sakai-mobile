package com.example.noellesun.sakai;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.CookieManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static android.content.Intent.getIntent;

public class Notification extends Service {
    private String TAG = Notification.class.getSimpleName();
    //get assignment
    private ArrayList<String> asnList = new ArrayList<>();
    String fixurl = "https://sakai.duke.edu/direct/assignment/site/";
    String siteid;
    String cookiestr;
    public Notification() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //set cookies in order to maintain the same session
        final CookieManager cookieManager = CookieManager.getInstance();
        cookiestr = cookieManager.getCookie("https://sakai.duke.edu/portal");
        Log.d("Notification", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Notification", "onStart");
        Log.d("cookie", cookiestr);
        final ArrayList<String> siteids = intent.getExtras().getStringArrayList("ID_ARRAY");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //get gradebook, annoncement, assignment from server every 4 hours, check the time,
                //if post time is within 4 hours , send notification
                while(true){
                    try {
                        Thread.sleep(3600000); // every one hour
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Iterator<String> it1 = siteids.iterator();
                    for(int i = 1;i<siteids.size();i++) {
                        siteid = siteids.get(i);
                        if (!asnList.isEmpty()) {
                            //since don't know the method to deep copy the arraylist, just compare the size of the list to find the difference
                            int previousSize = asnList.size();
                            asnList.clear();
                            new Notification.GetAssign().execute();
                            int newSize = asnList.size();
                            if (newSize != previousSize) {
                                sendNotification();
                            }
                        } else {
                            new Notification.GetAssign().execute();
                        }
                    }
                }
            }
        }).start();return super.onStartCommand(intent, flags, startId);

    }

    private void sendNotification(){
        Log.d("Notification","send noti");
        Intent notificationIntent = new Intent(this, Login.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        android.app.Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Sakai: Homework has been updated")
                .setContentText("check it out")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .build();
        manager.notify(1, notification);
    }


    private class GetAssign extends AsyncTask< Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String url = fixurl + siteid + ".json";
            Log.i("assign_url",url);
            Log.i("assign_cookiestr",cookiestr);
            String jsonStr = sh.makeServiceCall(url, cookiestr);
            Log.e(TAG, "ASSIGNJSON: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray assignments = jsonObj.getJSONArray("assignment_collection");
                    for (int i = 0; i < assignments.length(); i++) {
                        JSONObject c = assignments.getJSONObject(i);
                        //get variable needed from JSON object
                        String itemName = c.getString("entityTitle");
                        Log.e("ASSINITEMNAME", itemName);
                        asnList.add(itemName);
                        Log.i("ASNLIST",asnList.toString());
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }
            Log.e("background","done!");
            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Notification", "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
