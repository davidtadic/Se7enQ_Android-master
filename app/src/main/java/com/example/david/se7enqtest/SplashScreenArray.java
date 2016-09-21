package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenArray extends Activity {

    //private static int SPLASH_TIME_OUT = 1500;
    TextView round;
    TextView array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_array);

        round = (TextView)findViewById(R.id.round3);
        array = (TextView)findViewById(R.id.array);


        /*new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreenArray.this, PlayArrayActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);*/
    }
}
