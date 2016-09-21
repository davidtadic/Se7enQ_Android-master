package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.DefinitionModel;
import com.example.david.se7enqtest.models.SynonymsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SynonymsActivity extends Activity {

    TextView questionRemain;
    TextView countdown;
    TextView description;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    SynonymsModel synonymsModel;
    CountDownTimer timer;
    int counter = 20;
    int counterButtons;
    ArrayList<SynonymsModel> arraySynonym = new ArrayList<>(20);
    ArrayList<SynonymsModel> tempQuestionSet = new ArrayList<>(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synonyms);

        questionRemain = (TextView)findViewById(R.id.questionsRemainSynonym);
        countdown = (TextView)findViewById(R.id.countdownSynonym);
        description = (TextView)findViewById(R.id.descriptionSynonym);
        answer1 = (Button)findViewById(R.id.answer1SynonymsTraining);
        answer2 = (Button)findViewById(R.id.answer2SynonymsTraining);
        answer3 = (Button)findViewById(R.id.answer3SynonymsTraining);
        answer4 = (Button)findViewById(R.id.answer4SynonymsTraining);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<SynonymsModel>> synonymCall = service.getSynonym();

        synonymCall.enqueue(new Callback<List<SynonymsModel>>() {
            @Override
            public void onResponse(Call<List<SynonymsModel>> call, Response<List<SynonymsModel>> response) {
                Log.i("Sinonimi Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        synonymsModel = new SynonymsModel();

                        synonymsModel.setId(response.body().get(i).getId());
                        synonymsModel.setCorrectAnswer1(response.body().get(i).getCorrectAnswer1());
                        synonymsModel.setCorrectAnswer2(response.body().get(i).getCorrectAnswer2());
                        synonymsModel.setWrongAnswer1(response.body().get(i).getWrongAnswer1());
                        synonymsModel.setWrongAnswer2(response.body().get(i).getWrongAnswer2());



                        arraySynonym.add(synonymsModel);
                    }


                    for(SynonymsModel synonymsModel:arraySynonym){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(synonymsModel);
                        setAnswer(synonymsModel);
                        checkAnswer(synonymsModel);
                        startTimer();
                        break;
                    }

                }
                else if(response.code() == 400){
                    finish();
                    Toast toast = Toast.makeText(getBaseContext(), "You have been already trained in this category today\nCome back tomorrow", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    if( v != null) v.setGravity(Gravity.CENTER);
                    toast.show();
                }
                else {
                    Log.e("Error, Sinonimi!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<SynonymsModel>> call, Throwable t) {
                Log.e("Sinonimi ", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setAnswer(SynonymsModel synonymsModel){
        //mix answers
        List<String> answers = synonymsModel.getQuestionOptions();

        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
    }

    private void startTimer(){

        timer = new CountDownTimer(7000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText("Time left: "
                        + String.valueOf(millisUntilFinished / 1000));
            }
            @Override
            public void onFinish() {
                clearColors();
                counter--;
                questionRemain.setText("Questions remain: "+counter);
                final SynonymsModel synonymsModel = getQuestionSet();
                if(synonymsModel == null){
                    timer.cancel();

                    //info dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(SynonymsActivity.this);
                    TextView myMsg = new TextView(SynonymsActivity.this);
                    myMsg.setText("You have been trained in this category for today\nSee you tomorrow");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(SynonymsActivity.this, TrainingActivity.class));
                                    finish();

                                }
                            }).setView(myMsg);

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAnswer(synonymsModel);
                            checkAnswer(synonymsModel);
                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }

    private SynonymsModel getQuestionSet(){
        SynonymsModel newQuestion = null;
        for (final SynonymsModel synonymsModel : arraySynonym) {
            if(tempQuestionSet.contains(synonymsModel)){
            }
            else{
                tempQuestionSet.add(synonymsModel);
                return synonymsModel;
            }
        }

        return newQuestion;
    }

    private void checkAnswer(SynonymsModel synonymsModel){

        final String correct1 = synonymsModel.getCorrectAnswer1();
        final String correct2 = synonymsModel.getCorrectAnswer2();
        counterButtons = 0;


        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer1.getText().toString().equals(correct1) || answer1.getText().toString().equals(correct2))){
                    answer1.setBackgroundColor(Color.RED);
                    answer1.setTextColor(Color.BLACK);

                    counterButtons = 0;

                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            answer1.setTextColor(Color.BLACK);
                            answer1.setBackgroundColor(Color.parseColor("#979292"));
                        }
                    }, 300);

                    timer.onFinish();

                }
                else{
                    counterButtons++;
                    answer1.setBackgroundColor(Color.GREEN);
                    answer1.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        timer.onFinish();
                        new Handler().postDelayed(new Runnable() {

                            public void run() {
                                answer1.setTextColor(Color.BLACK);
                                answer1.setBackgroundColor(Color.parseColor("#979292"));
                            }
                        }, 300);
                    }
                }


            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer2.getText().toString().equals(correct1) || answer2.getText().toString().equals(correct2))){
                    answer2.setBackgroundColor(Color.RED);
                    answer2.setTextColor(Color.BLACK);

                    counterButtons = 0;
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            answer2.setTextColor(Color.BLACK);
                            answer2.setBackgroundColor(Color.parseColor("#979292"));
                        }
                    }, 300);
                    timer.onFinish();

                }
                else{
                    counterButtons++;
                    answer2.setBackgroundColor(Color.GREEN);
                    answer2.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        new Handler().postDelayed(new Runnable() {

                            public void run() {
                                answer2.setTextColor(Color.BLACK);
                                answer2.setBackgroundColor(Color.parseColor("#979292"));
                            }
                        }, 300);
                        timer.onFinish();

                    }
                }



            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer3.getText().toString().equals(correct1) || answer3.getText().toString().equals(correct2))){
                    answer3.setBackgroundColor(Color.RED);
                    answer3.setTextColor(Color.BLACK);

                    counterButtons = 0;
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            answer3.setTextColor(Color.BLACK);
                            answer3.setBackgroundColor(Color.parseColor("#979292"));
                        }
                    }, 300);

                    timer.onFinish();
                }
                else{
                    counterButtons++;
                    answer3.setBackgroundColor(Color.GREEN);
                    answer3.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        new Handler().postDelayed(new Runnable() {

                            public void run() {
                                answer3.setTextColor(Color.BLACK);
                                answer3.setBackgroundColor(Color.parseColor("#979292"));
                            }
                        }, 300);
                        timer.onFinish();
                    }
                }


            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer4.getText().toString().equals(correct1) || answer4.getText().toString().equals(correct2))){
                    answer4.setBackgroundColor(Color.RED);
                    answer4.setTextColor(Color.BLACK);

                    counterButtons = 0;
                    new Handler().postDelayed(new Runnable() {

                        public void run() {
                            answer4.setTextColor(Color.BLACK);
                            answer4.setBackgroundColor(Color.parseColor("#979292"));
                        }
                    }, 300);
                    timer.onFinish();
                }
                else{
                    counterButtons++;
                    answer4.setBackgroundColor(Color.GREEN);
                    answer4.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        new Handler().postDelayed(new Runnable() {

                            public void run() {
                                answer4.setTextColor(Color.BLACK);
                                answer4.setBackgroundColor(Color.parseColor("#979292"));
                            }
                        }, 300);
                        timer.onFinish();
                    }
                }


            }
        });

    }

    private void clearColors(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                answer1.setTextColor(Color.BLACK);
                answer1.setBackgroundColor(Color.parseColor("#979292"));
                answer2.setTextColor(Color.BLACK);
                answer2.setBackgroundColor(Color.parseColor("#979292"));
                answer3.setTextColor(Color.BLACK);
                answer3.setBackgroundColor(Color.parseColor("#979292"));
                answer4.setTextColor(Color.BLACK);
                answer4.setBackgroundColor(Color.parseColor("#979292"));
            }
        }, 300);

    }
}
