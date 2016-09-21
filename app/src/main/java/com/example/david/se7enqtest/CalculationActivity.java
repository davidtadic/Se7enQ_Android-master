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
import com.example.david.se7enqtest.models.ArrayModel;
import com.example.david.se7enqtest.models.CalculationModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculationActivity extends Activity {

    TextView questionRemain;
    TextView countdown;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    CalculationModel calculationModel;
    CountDownTimer timer;
    int counter = 20;
    ArrayList<CalculationModel> arrayCalculation = new ArrayList<>(20);
    ArrayList<CalculationModel> tempQuestionSet = new ArrayList<>(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);


        questionRemain = (TextView)findViewById(R.id.questionsRemainCalculation);
        countdown = (TextView)findViewById(R.id.countdownCalculation);
        question = (TextView)findViewById(R.id.questionCalculation);
        answer1 = (Button)findViewById(R.id.answer1CalculationTraining);
        answer2 = (Button)findViewById(R.id.answer2CalculationTraining);
        answer3 = (Button)findViewById(R.id.answer3CalculationTraining);
        answer4 = (Button)findViewById(R.id.answer4CalculationTraining);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<CalculationModel>> calculationCall = service.getCalculation();

        calculationCall.enqueue(new Callback<List<CalculationModel>>() {
            @Override
            public void onResponse(Call<List<CalculationModel>> call, Response<List<CalculationModel>> response) {
                Log.i("Calculation Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        calculationModel = new CalculationModel();

                        calculationModel.setId(response.body().get(i).getId());
                        calculationModel.setExpression(response.body().get(i).getExpression());
                        calculationModel.setCorrectResult(response.body().get(i).getCorrectResult());
                        calculationModel.setWrongResult1(response.body().get(i).getWrongResult1());
                        calculationModel.setWrongResult2(response.body().get(i).getWrongResult2());
                        calculationModel.setWrongResult3(response.body().get(i).getWrongResult3());

                        arrayCalculation.add(calculationModel);
                    }


                    for(CalculationModel calculationModel:arrayCalculation){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(calculationModel);
                        setAnswer(calculationModel);
                        checkAnswer(calculationModel);
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
                    Log.e("Error, Calculation!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<CalculationModel>> call, Throwable t) {
                Log.e("Calculation greska", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setAnswer(CalculationModel calculationModel){
        //mix answers
        List<Integer> answers = calculationModel.getQuestionOptions();

        question.setText(calculationModel.getExpression());
        answer1.setText(String.valueOf(answers.get(0)));
        answer2.setText(String.valueOf(answers.get(1)));
        answer3.setText(String.valueOf(answers.get(2)));
        answer4.setText(String.valueOf(answers.get(3)));
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
                final CalculationModel calculationModel = getQuestionSet();
                if(calculationModel == null){
                    timer.cancel();

                    //info dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(CalculationActivity.this);
                    TextView myMsg = new TextView(CalculationActivity.this);
                    myMsg.setText("You have been trained in this category for today\nSee you tomorrow");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(CalculationActivity.this, TrainingActivity.class));
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
                            setAnswer(calculationModel);
                            checkAnswer(calculationModel);
                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }


    private CalculationModel getQuestionSet(){
        CalculationModel newQuestion = null;
        for (final CalculationModel calculationModel : arrayCalculation) {
            if(tempQuestionSet.contains(calculationModel)){
            }
            else{
                tempQuestionSet.add(calculationModel);
                return calculationModel;
            }
        }

        return newQuestion;
    }

    public void checkAnswer(CalculationModel calculationModel){

        final String correct = Integer.toString(calculationModel.getCorrectResult());

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
