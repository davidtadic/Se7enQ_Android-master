package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenSynonyms extends Activity {

    private static int SPLASH_TIME_OUT = 1500;
    TextView round;
    TextView synonym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_synonyms);

        round = (TextView)findViewById(R.id.round1);
        synonym = (TextView)findViewById(R.id.synonyms);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreenSynonyms.this, PlaySynonymsActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
