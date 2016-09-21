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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefinitionsAcitivity extends Activity {

    TextView questionRemain;
    TextView countdown;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    DefinitionModel definitionModel;
    CountDownTimer timer;
    int counter = 20;
    ArrayList<DefinitionModel> arrayDefinition = new ArrayList<>(20);
    ArrayList<DefinitionModel> tempQuestionSet = new ArrayList<>(20);



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);

        questionRemain = (TextView)findViewById(R.id.questionsRemain);
        countdown = (TextView)findViewById(R.id.countdown);
        question = (TextView)findViewById(R.id.question);
        answer1 = (Button)findViewById(R.id.answer1);
        answer2 = (Button)findViewById(R.id.answer2);
        answer3 = (Button)findViewById(R.id.answer3);
        answer4 = (Button)findViewById(R.id.answer4);


        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<DefinitionModel>>  wordDefinitionsCall = service.getWordDefinitions();

        wordDefinitionsCall.enqueue(new Callback<List<DefinitionModel>>() {
            @Override
            public void onResponse(Call<List<DefinitionModel>> call, Response<List<DefinitionModel>> response) {
                Log.i("Definicije Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        definitionModel = new DefinitionModel();

                        definitionModel.setId(response.body().get(i).getId());
                        definitionModel.setCorrectAnswer(response.body().get(i).getCorrectAnswer());
                        definitionModel.setWrongAnswer1(response.body().get(i).getWrongAnswer1());
                        definitionModel.setWrongAnswer2(response.body().get(i).getWrongAnswer2());
                        definitionModel.setWrongAnswer3(response.body().get(i).getWrongAnswer3());
                        definitionModel.setWord(response.body().get(i).getWord());

                        arrayDefinition.add(definitionModel);
                    }


                    for(DefinitionModel wd:arrayDefinition){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(wd);
                        setAnswer(wd);
                        checkAnswer(wd);
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
                    Log.e("Error, Definitions!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<DefinitionModel>> call, Throwable t) {
                Log.e("DefinicijeFailure", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });




    }


    public void setAnswer(DefinitionModel wd){
        //mix answers
        List<String> answers = wd.getQuestionOptions();

        question.setText(wd.getWord());
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
                counter--;
                questionRemain.setText("Questions remain: "+counter);
                final DefinitionModel wd = getQuestionSet();
                if(wd == null){
                    timer.cancel();

                    //info dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(DefinitionsAcitivity.this);
                    TextView myMsg = new TextView(DefinitionsAcitivity.this);
                    myMsg.setText("You have been trained in this category for today\nSee you tomorrow");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(DefinitionsAcitivity.this, TrainingActivity.class));
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
                            setAnswer(wd);
                            checkAnswer(wd);
                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }

    private DefinitionModel getQuestionSet(){
        DefinitionModel newQuestion = null;
        for (final DefinitionModel wd : arrayDefinition) {
            if(tempQuestionSet.contains(wd)){
            }
            else{
                tempQuestionSet.add(wd);
                return wd;
            }
        }

        return newQuestion;
    }

    public void checkAnswer(DefinitionModel wd){

        final String correct = wd.getCorrectAnswer();

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer1.getText().toString().equals(correct)){
                    answer1.setBackgroundColor(Color.GREEN);
                    answer1.setTextColor(Color.BLACK);
                }else{
                    answer1.setBackgroundColor(Color.RED);
                    answer1.setTextColor(Color.BLACK);
                }

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer1.setTextColor(Color.BLACK);
                        answer1.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        timer.onFinish();

                    }
                }, 300);


            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer2.getText().toString().equals(correct)){
                    answer2.setBackgroundColor(Color.GREEN);
                    answer2.setTextColor(Color.BLACK);

                }else{
                    answer2.setBackgroundColor(Color.RED);
                    answer2.setTextColor(Color.BLACK);
                }

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer2.setTextColor(Color.BLACK);
                        answer2.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);


                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        timer.onFinish();

                    }
                }, 300);


            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer3.getText().toString().equals(correct)){
                    answer3.setBackgroundColor(Color.GREEN);
                    answer3.setTextColor(Color.BLACK);

                }else{
                    answer3.setBackgroundColor(Color.RED);
                    answer3.setTextColor(Color.BLACK);
                }

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer3.setTextColor(Color.BLACK);
                        answer3.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);


                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        timer.onFinish();

                    }
                }, 300);


            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer4.getText().toString().equals(correct)){
                    answer4.setBackgroundColor(Color.GREEN);
                    answer4.setTextColor(Color.BLACK);

                }else{
                    answer4.setBackgroundColor(Color.RED);
                    answer4.setTextColor(Color.BLACK);
                }

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer4.setTextColor(Color.BLACK);
                        answer4.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);

                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        timer.onFinish();

                    }
                }, 300);


            }
        });

    }

}
