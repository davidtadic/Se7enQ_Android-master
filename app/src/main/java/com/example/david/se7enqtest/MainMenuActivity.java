package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainMenuActivity extends Activity {

    TextView userName;
    TextView logout;
    Button exitButton;
    Button statisticsButton;
    Button training;
    Button  play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userName = (TextView)findViewById(R.id.userName);
        logout = (TextView)findViewById(R.id.logOut);

        SharedPreferences preferences = getSharedPreferences("MY_PREF", 0);
        String userNameString = preferences.getString("USERNAME", "");

        userName.setText(userNameString);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, LogInActivity.class));
                finish();
            }
        });

    }



    public void onExitButtonClickListener(View v){
        exitButton = (Button)findViewById(R.id.MainMenuExitButton);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenuActivity.this);

                alertDialog.setMessage("Are you sure you want to quit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = alertDialog.create();
                alert.show();

    }


    public void trainingListener(View v){
        training = (Button)findViewById(R.id.MainMenuTrainingButton);

        Intent intent = new Intent(MainMenuActivity.this, TrainingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}
