package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class SplashScreenDefinitions extends Activity {

    private static int SPLASH_TIME_OUT = 1500;
    TextView round;
    TextView definition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_definitions);

        round = (TextView)findViewById(R.id.round2);
        definition = (TextView) findViewById(R.id.definition);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreenDefinitions.this, PlayDefinitionsActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
