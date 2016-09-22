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
import com.example.david.se7enqtest.models.DefinitionModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArrayActivity extends Activity {
    TextView questionRemain;
    TextView countdown;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    ArrayModel arrayModel;
    CountDownTimer timer;
    int counter = 20;
    ArrayList<ArrayModel> arrayLogicArray = new ArrayList<>(20);
    ArrayList<ArrayModel> tempQuestionSet = new ArrayList<>(20);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array);

        questionRemain = (TextView)findViewById(R.id.questionsRemainArray);
        countdown = (TextView)findViewById(R.id.countdownArray);
        question = (TextView)findViewById(R.id.questionArray);
        answer1 = (Button)findViewById(R.id.answer1ArrayTraining);
        answer2 = (Button)findViewById(R.id.answer2ArrayTraining);
        answer3 = (Button)findViewById(R.id.answer3ArrayTraining);
        answer4 = (Button)findViewById(R.id.answer4ArrayTraining);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<ArrayModel>> arrayCall = service.getArray();

        arrayCall.enqueue(new Callback<List<ArrayModel>>() {
            @Override
            public void onResponse(Call<List<ArrayModel>> call, Response<List<ArrayModel>> response) {
                Log.i("Array Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        arrayModel = new ArrayModel();

                        arrayModel.setId(response.body().get(i).getId());
                        arrayModel.setArray(response.body().get(i).getArray());
                        arrayModel.setCorrectNumber(response.body().get(i).getCorrectNumber());
                        arrayModel.setWrongNumber1(response.body().get(i).getWrongNumber1());
                        arrayModel.setWrongNumber2(response.body().get(i).getWrongNumber2());
                        arrayModel.setWrongNumber3(response.body().get(i).getWrongNumber3());

                        arrayLogicArray.add(arrayModel);
                    }


                    for(ArrayModel arrayModel:arrayLogicArray){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(arrayModel);
                        setAnswer(arrayModel);
                        checkAnswer(arrayModel);
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
                    Log.e("Error, Array!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<ArrayModel>> call, Throwable t) {
                Log.e("Array greska", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAnswer(ArrayModel arrayModel){
        //mix answers
        List<String> answers = arrayModel.getQuestionOptions();

        question.setText(arrayModel.getArray());
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
                final ArrayModel arrayModel = getQuestionSet();
                if(arrayModel == null){
                    timer.cancel();

                    //info dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ArrayActivity.this);
                    TextView myMsg = new TextView(ArrayActivity.this);
                    myMsg.setText("You have been trained in this category for today\nSee you tomorrow");
                    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

                    builder.setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ArrayActivity.this, TrainingActivity.class));
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
                            setAnswer(arrayModel);
                            checkAnswer(arrayModel);
                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }

    private ArrayModel getQuestionSet(){
        ArrayModel newQuestion = null;
        for (final ArrayModel arrayModel : arrayLogicArray) {
            if(tempQuestionSet.contains(arrayModel)){
            }
            else{
                tempQuestionSet.add(arrayModel);
                return arrayModel;
            }
        }

        return newQuestion;
    }

    public void checkAnswer(ArrayModel arrayModel){

        final String correct = Integer.toString(arrayModel.getCorrectNumber());

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
