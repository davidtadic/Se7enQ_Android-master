package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.AnswerModel;
import com.example.david.se7enqtest.models.ArrayModel;
import com.example.david.se7enqtest.models.CalculationModel;
import com.example.david.se7enqtest.models.ReceiveQuestionArrayModel;
import com.example.david.se7enqtest.models.ReceiveQuestionCalculationModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayCalculationActivity extends Activity {

    TextView score;
    TextView countdown;
    TextView opponentScore;
    TextView expression;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    int questionIndex = 12;
    CountDownTimer timer;
    ApiCall service;
    String userAnswer = "1";
    boolean correct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_calculation);

        score = (TextView)findViewById(R.id.score4);
        countdown = (TextView)findViewById(R.id.countdown4);
        opponentScore = (TextView)findViewById(R.id.opponentScore4);
        expression = (TextView)findViewById(R.id.playCalculation);
        answer1 = (Button)findViewById(R.id.answer1PlayCalculation);
        answer2 = (Button)findViewById(R.id.answer2PlayCalculation);
        answer3 = (Button)findViewById(R.id.answer3PlayCalculation);
        answer4 = (Button)findViewById(R.id.answer4PlayCalculation);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        final String userToken = settings.getString("TOKEN","");

        //getting answer model from array
        SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
        boolean correctFirst = settingsAnswer.getBoolean("ANSWER_CORRECT_ARRAY",false);
        String answerFirst = settingsAnswer.getString("ANSWER_ARRAY","1");

        AnswerModel answerModel1 = new AnswerModel();
        answerModel1.setAnswer(answerFirst);
        answerModel1.setCorrect(correctFirst);
        answerModel1.setQuestionIndex(12);

        //making a service
        service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        //start quiz
        getQuestion(answerModel1);
        startQuestion();


    }

    private AnswerModel getAnswerModel(int questionIndex){
        AnswerModel answerModel = new AnswerModel();
        answerModel.setAnswer(userAnswer);
        answerModel.setCorrect(correct);
        answerModel.setQuestionIndex(questionIndex);

        return answerModel;
    }

    private  void startQuestion(){
        questionIndex = 12;

        timer = new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                questionIndex++;
                final AnswerModel answerModel = getAnswerModel(questionIndex);
                if(questionIndex == 16){
                    SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
                    SharedPreferences.Editor editorAnswer = settingsAnswer.edit();
                    editorAnswer.putBoolean("ANSWER_CORRECT_CALCULATION", correct);
                    editorAnswer.putString("ANSWER_CALCULATION", userAnswer);
                    editorAnswer.commit();

                    timer.cancel();
                    startActivity(new Intent(PlayCalculationActivity.this, SplashScreenKnowledge.class));
                    finish();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getQuestion(answerModel);

                        }
                    });
                    timer.start();
                }
            }
        }.start();


    }

    public void setAnswer(CalculationModel calculationModel){
        //mix answers
        List<String> answers = calculationModel.getQuestionOptions();

        expression.setText(calculationModel.getExpression());
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
    }

    public void getQuestion(AnswerModel answerModel){

        Call<ReceiveQuestionCalculationModel> receiveQuestionCall = service.receiveQuestionCalculation(answerModel);

        receiveQuestionCall.enqueue(new Callback<ReceiveQuestionCalculationModel>() {
            @Override
            public void onResponse(Call<ReceiveQuestionCalculationModel> call, Response<ReceiveQuestionCalculationModel> response) {

                if(response.isSuccessful()){

                    if(response.body().toString() != null) {
                        Log.e("Response calc.", response.body().toString());
                    }
                    returnColor();
                    setEnabledButtons();

                    final CalculationModel calculationModel = response.body().getQuestion();
                    int playerPoints = response.body().getPlayerPoints();
                    final int opponentPoints = response.body().getOpponentPoints();
                    final String opponentAnswer = response.body().getOpponentAnswer();



                    setAnswer(calculationModel);
                    score.setText("My score: "+String.valueOf(playerPoints));
                    opponentScore.setText("Opponent\n score: "+String.valueOf(opponentPoints));
                    checkAnswer(calculationModel);



                    //showOpponentsAnswer(opponentAnswer);



                }
                else{
                    if(response.message() != null) {
                        Log.e("Response calc.", response.message());
                    }
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReceiveQuestionCalculationModel> call, Throwable t) {
                if(t.getMessage() != null) {
                    Log.e("Calc", t.getMessage());
                }
                timer.cancel();
                finish();

                Toast.makeText(getBaseContext(), "Sorry, but there is an error!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PlayCalculationActivity.this, MainMenuActivity.class));
            }
        });

    }

    public boolean checkAnswer(CalculationModel calculationModel){

        final String correctAnswer = Integer.toString(calculationModel.getCorrectResult());
        correct = false;

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer1.getText().toString().equals(correctAnswer)){
                    answer1.setBackgroundColor(Color.GREEN);
                    answer1.setTextColor(Color.BLACK);

                    correct = true;
                }else{
                    answer1.setBackgroundColor(Color.RED);
                    answer1.setTextColor(Color.BLACK);

                    correct = false;
                }

                setDisabledButtons();
                userAnswer = answer1.getText().toString();
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer2.getText().toString().equals(correctAnswer)){
                    answer2.setBackgroundColor(Color.GREEN);
                    answer2.setTextColor(Color.BLACK);

                    correct = true;

                }else{
                    answer2.setBackgroundColor(Color.RED);
                    answer2.setTextColor(Color.BLACK);

                    correct = false;
                }

                setDisabledButtons();
                userAnswer = answer2.getText().toString();

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer3.getText().toString().equals(correctAnswer)){
                    answer3.setBackgroundColor(Color.GREEN);
                    answer3.setTextColor(Color.BLACK);

                    correct = true;
                }else{
                    answer3.setBackgroundColor(Color.RED);
                    answer3.setTextColor(Color.BLACK);

                    correct = false;
                }

                setDisabledButtons();
                userAnswer = answer3.getText().toString();

            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answer4.getText().toString().equals(correctAnswer)){
                    answer4.setBackgroundColor(Color.GREEN);
                    answer4.setTextColor(Color.BLACK);

                    correct = true;
                }else{
                    answer4.setBackgroundColor(Color.RED);
                    answer4.setTextColor(Color.BLACK);

                    correct = false;
                }

                setDisabledButtons();
                userAnswer = answer4.getText().toString();
            }
        });

        return correct;
    }

    private void showOpponentsAnswer(String opponentAnswer){
        if(answer1.getText().toString().equals(opponentAnswer)){
            answer1.setBackgroundColor(Color.YELLOW);
            answer1.setTextColor(Color.BLACK);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer1.setTextColor(Color.BLACK);
                    answer1.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);
        }
        else if(answer2.getText().toString().equals(opponentAnswer)){
            answer2.setBackgroundColor(Color.YELLOW);
            answer2.setTextColor(Color.BLACK);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer2.setTextColor(Color.BLACK);
                    answer2.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);
        }
        else if(answer3.getText().toString().equals(opponentAnswer)){
            answer3.setBackgroundColor(Color.YELLOW);
            answer3.setTextColor(Color.BLACK);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer3.setTextColor(Color.BLACK);
                    answer3.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);
        }
        else if(answer4.getText().toString().equals(opponentAnswer)){
            answer4.setBackgroundColor(Color.YELLOW);
            answer4.setTextColor(Color.BLACK);

            new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer4.setTextColor(Color.BLACK);
                    answer4.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);
        }

    }

    private void setEnabledButtons(){
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);
        answer4.setEnabled(true);
    }

    private void setDisabledButtons(){
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        answer4.setEnabled(false);
    }

    private void returnColor(){
        answer1.setTextColor(Color.BLACK);
        answer1.setBackgroundColor(Color.parseColor("#979292"));
        answer2.setTextColor(Color.BLACK);
        answer2.setBackgroundColor(Color.parseColor("#979292"));
        answer3.setTextColor(Color.BLACK);
        answer3.setBackgroundColor(Color.parseColor("#979292"));
        answer4.setTextColor(Color.BLACK);
        answer4.setBackgroundColor(Color.parseColor("#979292"));
    }
}
