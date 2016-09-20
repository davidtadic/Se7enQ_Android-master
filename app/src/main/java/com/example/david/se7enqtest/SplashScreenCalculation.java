package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenCalculation extends Activity {

    private static int SPLASH_TIME_OUT = 1500;
    TextView round;
    TextView calculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_calculation);

        round = (TextView)findViewById(R.id.round4);
        calculation = (TextView)findViewById(R.id.calculation);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreenCalculation.this, PlayCalculationActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
