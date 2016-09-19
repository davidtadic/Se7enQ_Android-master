package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class PlayActivity extends Activity {

    TextView yourOpponent;
    TextView opponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        yourOpponent = (TextView)findViewById(R.id.yourOpponent);
        opponentName = (TextView)findViewById(R.id.opponentName);

        SharedPreferences preferences = getSharedPreferences("MY_PREF", 0);
        String opponentFirstName = preferences.getString("FIRST_NAME", "");

        opponentName.setText(opponentFirstName);

    }
}
