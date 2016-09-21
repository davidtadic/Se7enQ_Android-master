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
import com.example.david.se7enqtest.models.GeneralKnowledgeModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralKnowledgeActivity extends Activity {

    TextView questionRemain;
    TextView countdown;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    GeneralKnowledgeModel generalKnowledgeModel;
    CountDownTimer timer;
    int counter = 20;
    ArrayList<GeneralKnowledgeModel> arrayGK = new ArrayList<>(20);
    ArrayList<GeneralKnowledgeModel> tempQuestionSet = new ArrayList<>(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_knowledge);

        questionRemain = (TextView)findViewById(R.id.questionsRemainGK);
        countdown = (TextView)findViewById(R.id.countdownGK);
        question = (TextView)findViewById(R.id.questionGK);
        answer1 = (Button)findViewById(R.id.answer1GKTraining);
        answer2 = (Button)findViewById(R.id.answer2GKTraining);
        answer3 = (Button)findViewById(R.id.answer3GKTraining);
        answer4 = (Button)findViewById(R.id.answer4GKTraining);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<GeneralKnowledgeModel>> gKCall = service.getKnowledge();

        gKCall.enqueue(new Callback<List<GeneralKnowledgeModel>>() {
            @Override
            public void onResponse(Call<List<GeneralKnowledgeModel>> call, Response<List<GeneralKnowledgeModel>> response) {
                Log.i("GK Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        generalKnowledgeModel = new GeneralKnowledgeModel();

                        generalKnowledgeModel.setId(response.body().get(i).getId());
                        generalKnowledgeModel.setQuestion(response.body().get(i).getQuestion());
                        generalKnowledgeModel.setCorrectAnswer(response.body().get(i).getCorrectAnswer());
                        generalKnowledgeModel.setWrongAnswer1(response.body().get(i).getWrongAnswer1());
                        generalKnowledgeModel.setWrongAnswer2(response.body().get(i).getWrongAnswer2());
                        generalKnowledgeModel.setWrongAnswer3(response.body().get(i).getWrongAnswer3());

                        arrayGK.add(generalKnowledgeModel);
                    }


                    for(GeneralKnowledgeModel generalKnowledgeModel:arrayGK){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(generalKnowledgeModel);
                        setAnswer(generalKnowledgeModel);
                        checkAnswer(generalKnowledgeModel);
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
                    Log.e("Error, GK!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<GeneralKnowledgeModel>> call, Throwable t) {
                Log.e("GK", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAnswer(GeneralKnowledgeModel generalKnowledgeModel){
        //mix answers
        List<String> answers = generalKnowledgeModel.getQuestionOptions();

        question.setText(generalKnowledgeModel.getQuestion());
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
                final GeneralKnowledgeModel generalKnowledgeModel = getQuestionSet();
                if(generalKnowledgeModel == null){
                    timer.cancel();

                    //info dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(GeneralKnowledgeActivity.this);
                    TextView myMsg = new TextView(GeneralKnowledgeActivity.this);
                    myMsg.setText("You have been trained in this category for today\nSee you tomorrow");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(GeneralKnowledgeActivity.this, TrainingActivity.class));
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
                            setAnswer(generalKnowledgeModel);
                            checkAnswer(generalKnowledgeModel);
                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }

    private GeneralKnowledgeModel getQuestionSet(){
        GeneralKnowledgeModel newQuestion = null;
        for (final GeneralKnowledgeModel generalKnowledgeModel : arrayGK) {
            if(tempQuestionSet.contains(generalKnowledgeModel)){
            }
            else{
                tempQuestionSet.add(generalKnowledgeModel);
                return generalKnowledgeModel;
            }
        }

        return newQuestion;
    }

    public void checkAnswer(GeneralKnowledgeModel generalKnowledgeModel){

        final String correct = generalKnowledgeModel.getCorrectAnswer();

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
