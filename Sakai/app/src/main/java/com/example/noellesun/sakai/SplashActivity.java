package com.example.noellesun.sakai;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 900;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, Login.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);

    }
}