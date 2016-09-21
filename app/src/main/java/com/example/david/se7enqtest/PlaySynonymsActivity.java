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
import com.example.david.se7enqtest.models.ReceiveQuestionSynonymModel;
import com.example.david.se7enqtest.models.SynonymsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySynonymsActivity extends Activity{

    TextView score;
    TextView countdown;
    TextView opponentScore;
    TextView description;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    static int myPoints = 0;
    int questionIndex = 0;
    CountDownTimer timer;
    ApiCall service;
    String userAnswer = "first second";
    AnswerModel answerModel1;
    boolean correct;
    int counterButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_synonyms);

        score = (TextView)findViewById(R.id.score1);
        countdown = (TextView)findViewById(R.id.countdown1);
        opponentScore = (TextView)findViewById(R.id.opponentScore1);
        description = (TextView)findViewById(R.id.textSynonyms);
        answer1 = (Button)findViewById(R.id.answer1Synonyms);
        answer2 = (Button)findViewById(R.id.answer2Synonyms);
        answer3 = (Button)findViewById(R.id.answer3Synonyms);
        answer4 = (Button)findViewById(R.id.answer4Synonyms);



        answerModel1 = new AnswerModel();
        answerModel1.setAnswer("first second");
        answerModel1.setCorrect(false);
        answerModel1.setQuestionIndex(0);


        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        final String userToken = settings.getString("TOKEN","");

        //making a service
        service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);


        //start quiz
        getQuestion(answerModel1);
        startQuestion();



        //save in my score
        SharedPreferences settingsUser = getSharedPreferences("MY_SCORE_PREF",0);
        SharedPreferences.Editor editorUser = settingsUser.edit();
        editorUser.putInt("MY_SCORE", myPoints);
        editorUser.commit();

    }




    private AnswerModel getAnswerModel(int questionIndex){
        AnswerModel answerModel = new AnswerModel();
        answerModel.setAnswer(userAnswer);
        answerModel.setCorrect(correct);
        answerModel.setQuestionIndex(questionIndex);

        return answerModel;
    }

    private  void startQuestion(){
        questionIndex = 0;

        timer = new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                returnColor();
                questionIndex++;
                final AnswerModel answerModel = getAnswerModel(questionIndex);
                if(questionIndex == 4) {
                    SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
                    SharedPreferences.Editor editorAnswer = settingsAnswer.edit();
                    editorAnswer.putBoolean("ANSWER_CORRECT", correct);
                    editorAnswer.putString("ANSWER", userAnswer);
                    editorAnswer.commit();

                    timer.cancel();
                    startActivity(new Intent(PlaySynonymsActivity.this, SplashScreenDefinitions.class));
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

    public void setAnswer(SynonymsModel synonymQuestion){
        //mix answers
        List<String> answers = synonymQuestion.getQuestionOptions();

        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
    }


    public void getQuestion(AnswerModel answerModel){

        Call<ReceiveQuestionSynonymModel> receiveQuestionCall = service.receiveQuestionSynonym(answerModel);

        receiveQuestionCall.enqueue(new Callback<ReceiveQuestionSynonymModel>() {
            @Override
            public void onResponse(Call<ReceiveQuestionSynonymModel> call, Response<ReceiveQuestionSynonymModel> response) {

                if(response.isSuccessful()){

                if(response.body().toString() != null) {
                    Log.e("Response sinonim", response.body().toString());
                }


                    final SynonymsModel synonymQuestion = response.body().getQuestion();
                    final int opponentPoints = response.body().getOpponentPoints();
                    String opponentAnswer = response.body().getOpponentAnswer();

                    final String opponentAnswer1 = opponentAnswer.split(" ")[0];
                    final String opponentAnswer2 = opponentAnswer.split(" ")[1];

                    setEnabledButtons();
                    setAnswer(synonymQuestion);
                    opponentScore.setText("Opponent\n score: "+String.valueOf(opponentPoints));
                    checkAnswer(synonymQuestion);
                    if(checkAnswer(synonymQuestion)){
                        myPoints += 1;
                        score.setText("Score: "+String.valueOf(myPoints));
                    }
                    /*new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                        }
                    },2000);*/


                    //showOpponentsAnswer(opponentAnswer1,opponentAnswer2);


                    /*new Handler().postDelayed(new Runnable() {

                        public void run() {
                        }
                    }, 200);*/


                }
                else{
                    if(response.message() != null) {
                        Log.e("Response synonym", response.message());
                    }
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReceiveQuestionSynonymModel> call, Throwable t) {
                if(t.getMessage() != null) {
                    Log.e("Sinonimi", t.getMessage());
                }
                finish();

                Toast.makeText(getBaseContext(), "Sorry, but there is an error!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PlaySynonymsActivity.this, MainMenuActivity.class));
            }
        });

    }

    private boolean checkAnswer(SynonymsModel synonymsModel){

        final String correct1 = synonymsModel.getCorrectAnswer1();
        final String correct2 = synonymsModel.getCorrectAnswer2();
        counterButtons = 0;
        correct = false;


        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer1.getText().toString().equals(correct1) || answer1.getText().toString().equals(correct2))){
                    answer1.setBackgroundColor(Color.RED);
                    answer1.setTextColor(Color.BLACK);

                    answer2.setEnabled(false);
                    answer3.setEnabled(false);
                    answer4.setEnabled(false);

                    counterButtons = 0;
                    correct = false;
                }
                else{
                    counterButtons++;
                    answer1.setBackgroundColor(Color.GREEN);
                    answer1.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        correct = true;

                    }
                }
                userAnswer = answer1.getText().toString()+" second";


                /*new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer1.setTextColor(Color.BLACK);
                        answer1.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);*/
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer2.getText().toString().equals(correct1) || answer2.getText().toString().equals(correct2))){
                    answer2.setBackgroundColor(Color.RED);
                    answer2.setTextColor(Color.BLACK);

                    answer1.setEnabled(false);
                    answer3.setEnabled(false);
                    answer4.setEnabled(false);

                    counterButtons = 0;
                    correct = false;
                }
                else{
                    counterButtons++;
                    answer2.setBackgroundColor(Color.GREEN);
                    answer2.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        correct = true;
                    }
                }
                userAnswer = answer2.getText().toString()+" second";


                /*new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer2.setTextColor(Color.BLACK);
                        answer2.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);*/
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer3.getText().toString().equals(correct1) || answer3.getText().toString().equals(correct2))){
                    answer3.setBackgroundColor(Color.RED);
                    answer3.setTextColor(Color.BLACK);

                    answer1.setEnabled(false);
                    answer2.setEnabled(false);
                    answer4.setEnabled(false);

                    counterButtons = 0;
                    correct = false;
                }
                else{
                    counterButtons++;
                    answer3.setBackgroundColor(Color.GREEN);
                    answer3.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        correct = true;
                    }
                }
                userAnswer = answer3.getText().toString()+" second";

                /*new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer3.setTextColor(Color.BLACK);
                        answer3.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);*/
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(answer4.getText().toString().equals(correct1) || answer4.getText().toString().equals(correct2))){
                    answer4.setBackgroundColor(Color.RED);
                    answer4.setTextColor(Color.BLACK);

                    answer1.setEnabled(false);
                    answer2.setEnabled(false);
                    answer3.setEnabled(false);

                    counterButtons = 0;
                    correct = false;
                }
                else{
                    counterButtons++;
                    answer4.setBackgroundColor(Color.GREEN);
                    answer4.setTextColor(Color.BLACK);

                    if(counterButtons == 2){
                        correct = true;
                    }
                }
                userAnswer = answer4.getText().toString()+" second";

                /*new Handler().postDelayed(new Runnable() {

                    public void run() {
                        answer4.setTextColor(Color.BLACK);
                        answer4.setBackgroundColor(Color.parseColor("#979292"));
                    }
                }, 300);*/
            }
        });

        return correct;
    }

    private void showOpponentsAnswer(String opponentAnswer1, String opponentAnswer2){

        if(answer1.getText().toString().equals(opponentAnswer1) || answer1.getText().toString().equals(opponentAnswer2)){
            answer1.setBackgroundColor(Color.YELLOW);
            answer1.setTextColor(Color.BLACK);

            /*new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer4.setTextColor(Color.BLACK);
                    answer4.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);*/
        }
        else if(answer2.getText().toString().equals(opponentAnswer1) || answer2.getText().toString().equals(opponentAnswer2)){
            answer2.setBackgroundColor(Color.YELLOW);
            answer2.setTextColor(Color.BLACK);

            /*new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer4.setTextColor(Color.BLACK);
                    answer4.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);*/
        }
        else if(answer3.getText().toString().equals(opponentAnswer1) || answer3.getText().toString().equals(opponentAnswer2)){
            answer2.setBackgroundColor(Color.YELLOW);
            answer2.setTextColor(Color.BLACK);

            /*new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer4.setTextColor(Color.BLACK);
                    answer4.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);*/
        }
        else if(answer4.getText().toString().equals(opponentAnswer1) || answer4.getText().toString().equals(opponentAnswer2)){
            answer2.setBackgroundColor(Color.YELLOW);
            answer2.setTextColor(Color.BLACK);

            /*new Handler().postDelayed(new Runnable() {

                public void run() {
                    answer4.setTextColor(Color.BLACK);
                    answer4.setBackgroundColor(Color.parseColor("#979292"));
                }
            }, 200);*/
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
