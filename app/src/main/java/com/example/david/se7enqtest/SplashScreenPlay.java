package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.UserRegister;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenPlay extends Activity {
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_play);

        progress = (ProgressBar)findViewById(R.id.progressBar);


        new LoadPlayMode().execute();

    }

    public class LoadPlayMode extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            progress = new ProgressBar(SplashScreenPlay.this);
            progress.setIndeterminate(true);
        }

        @Override
        protected Void doInBackground(Void... params) {


            //getting token out of shared preference
            SharedPreferences settings = getSharedPreferences("MY_PREF",0);
            String userToken = settings.getString("TOKEN","");

            ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);
            Call<UserRegister> findOpponentCall = service.findOpponent();

            findOpponentCall.enqueue(new Callback<UserRegister>() {
                @Override
                public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {

                    if(response.body() != null) {
                        Log.i("Play response", response.message() + " " + response.code() + " " + response.body().toString());
                    }

                    if(response.isSuccessful()) {

                        UserRegister user = response.body();

                        //name of opponent
                        SharedPreferences settings = getSharedPreferences("MY_PREF_NAME", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("FIRST_NAME", user.getFirstName());
                        editor.commit();

                        startActivity(new Intent(SplashScreenPlay.this, OpponentPlayActivity.class));
                        finish();



                    }
                    else{
                        Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {
                    if(t.getMessage() != null) {
                        Log.i("Play failure", t.getMessage());
                    }

                    if(t.getMessage().equals("timeout")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenPlay.this);
                        TextView myMsg = new TextView(SplashScreenPlay.this);
                        myMsg.setText("\nThere isn't players at the moment\nTry again later");
                        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                        builder.setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SplashScreenPlay.this, MainMenuActivity.class));
                                        finish();

                                    }
                                }).setView(myMsg);

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
            });

            return null;
        }



    }
}
