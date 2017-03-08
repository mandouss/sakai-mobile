package com.example.noellesun.sakai;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Assignment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        findViewById(R.id.btn8).setOnClickListener(siteClickEvent);
    }
    final OnClickListener siteClickEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(),"sites clicked",Toast.LENGTH_SHORT).show();
        }
    };

}


