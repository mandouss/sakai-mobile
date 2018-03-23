package com.example.noellesun.sakai;

/**
 * Created by Liu on 2017/3/26.
 */

import java.io.IOException;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

 public class HttpHandler { // Ethan: why can we us package protected
    private int bad;
    private static final String TAG = HttpHandler.class.getSimpleName();

    HttpHandler() {
        bad = 10;
    }

    String makeServiceCall(String reqUrl, String myCookie) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Use cookie to maintain the login status
            conn.setRequestProperty("Cookie", myCookie);
            Log.i("aftersetcookie","aftersetcookie");
            conn.setRequestMethod("GET");
            Log.i("aftersetget","aftersetget");
            // read the response
            bad = conn.getResponseCode();
            Log.e("BAD", String.valueOf(bad));
            InputStream in = new BufferedInputStream(conn.getInputStream());
            Log.i("inputstream",in.toString());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + Integer.toString( bad) );
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    //convert stream read from http connection into String
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
