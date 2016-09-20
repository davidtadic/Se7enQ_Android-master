package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class OpponentPlayActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;
    TextView yourOpponent;
    TextView opponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        yourOpponent = (TextView)findViewById(R.id.yourOpponent);
        opponentName = (TextView)findViewById(R.id.opponentName);

        SharedPreferences preferences = getSharedPreferences("MY_PREF_NAME", 0);
        String opponentFirstName = preferences.getString("FIRST_NAME", "");

        opponentName.setText(opponentFirstName);

        preferences.edit().remove("FIRST_NAME").commit();

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(OpponentPlayActivity.this, SplashScreenSynonyms.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
