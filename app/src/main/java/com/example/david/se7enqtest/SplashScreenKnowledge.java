package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashScreenKnowledge extends Activity {

    private static int SPLASH_TIME_OUT = 1500;
    TextView round;
    TextView knowledge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_knowledge);

        round = (TextView)findViewById(R.id.round5);
        knowledge = (TextView)findViewById(R.id.knowledge);


        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashScreenKnowledge.this, PlayKnowledgeActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
