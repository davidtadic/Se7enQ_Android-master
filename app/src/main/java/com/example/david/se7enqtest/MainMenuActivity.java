package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends Activity {

    Button exitButton;
    Button statisticsButton;
    Button training;
    Button  play;
    TextView logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        logOut = (TextView) findViewById(R.id.logOutTextView);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenuActivity.this);

                alertDialog.setMessage("Are you sure you want to logout?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(MainMenuActivity.this, LogInActivity.class);
                                startActivity(intent);
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
        });


    }

    public void onExitButtonClickListener(View v){
        exitButton = (Button)findViewById(R.id.MainMenuExitButton);


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainMenuActivity.this);

                alertDialog.setMessage("Are you sure you want to quit?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
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





}
