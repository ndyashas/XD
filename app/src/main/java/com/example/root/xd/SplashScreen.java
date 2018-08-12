package com.example.root.xd;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    public  class ConnectAndGetXDInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {

            // connect to the wifi on Raspberry pi


            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent homeIntent = new Intent(SplashScreen.this, LoginScreen.class);
            startActivity(homeIntent);
            finish();
            super.onPostExecute(aVoid);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        int SPLASH_TIME_OUT = 1000;
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
