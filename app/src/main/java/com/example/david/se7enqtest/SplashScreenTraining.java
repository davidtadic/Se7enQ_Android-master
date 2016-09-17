package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;

public class SplashScreenTraining extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_training);


        CountDownTimer timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {

                Intent intentBefore = getIntent();
                int intValue = intentBefore.getIntExtra("splash",0);

                switch (intValue){
                    case R.id.synonymsButton:
                        startActivity(new Intent(SplashScreenTraining.this, Synonyms.class));
                    break;
                    case R.id.definitionsButton:
                        startActivity(new Intent(SplashScreenTraining.this, Definitions.class));
                        break;
                    case R.id.arrayButton:
                        startActivity(new Intent(SplashScreenTraining.this, Array.class));
                        break;
                    case R.id.calculationsButton:
                        startActivity(new Intent(SplashScreenTraining.this, Calculation.class));
                        break;
                    case R.id.knowledgeButton:
                        startActivity(new Intent(SplashScreenTraining.this, GeneralKnowledge.class));
                        break;
                }

                finish();
            }
        };

        timer.start();

    }


}
