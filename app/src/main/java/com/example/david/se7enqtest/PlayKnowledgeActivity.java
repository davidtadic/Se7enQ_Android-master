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
import com.example.david.se7enqtest.models.CalculationModel;
import com.example.david.se7enqtest.models.DefinitionModel;
import com.example.david.se7enqtest.models.GeneralKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveQuestionCalculationModel;
import com.example.david.se7enqtest.models.ReceiveQuestionKnowledgeModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayKnowledgeActivity extends Activity {

    TextView score;
    TextView countdown;
    TextView opponentScore;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    int questionIndex = 16;
    CountDownTimer timer;
    ApiCall service;
    String userAnswer = "pera";
    AnswerModel answerModel1;
    boolean correct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_knowledge);

        score = (TextView)findViewById(R.id.score5);
        countdown = (TextView)findViewById(R.id.countdown5);
        opponentScore = (TextView)findViewById(R.id.opponentScore5);
        question = (TextView)findViewById(R.id.questionPlayKnowledge);
        answer1 = (Button)findViewById(R.id.answer1PlayKnowledge);
        answer2 = (Button)findViewById(R.id.answer2PlayKnowledge);
        answer3 = (Button)findViewById(R.id.answer3PlayKnowledge);
        answer4 = (Button)findViewById(R.id.answer4PlayKnowledge);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        final String userToken = settings.getString("TOKEN","");

        //getting answer model from calculations
        SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
        boolean correctFirst = settingsAnswer.getBoolean("ANSWER_CORRECT_CALCULATION",false);
        String answerFirst = settingsAnswer.getString("ANSWER_CALCULATION","");

        answerModel1 = new AnswerModel();
        answerModel1.setAnswer(answerFirst);
        answerModel1.setCorrect(correctFirst);
        answerModel1.setQuestionIndex(16);

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
        questionIndex = 16;

        timer = new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                questionIndex++;
                final AnswerModel answerModel = getAnswerModel(questionIndex);
                if(questionIndex == 20){
                    SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
                    SharedPreferences.Editor editorAnswer = settingsAnswer.edit();
                    editorAnswer.putBoolean("ANSWER_CORRECT_KNOWLEDGE", correct);
                    editorAnswer.putString("ANSWER_KNOWLEDGE", userAnswer);
                    editorAnswer.commit();

                    timer.cancel();
                    startActivity(new Intent(PlayKnowledgeActivity.this, PlayFinishActivity.class));
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

    public void setAnswer(GeneralKnowledgeModel generalKnowledgeModel){
        //mix answers
        List<String> answers = generalKnowledgeModel.getQuestionOptions();

        question.setText(generalKnowledgeModel.getQuestion());
        answer1.setText(String.valueOf(answers.get(0)));
        answer2.setText(String.valueOf(answers.get(1)));
        answer3.setText(String.valueOf(answers.get(2)));
        answer4.setText(String.valueOf(answers.get(3)));
    }

    public void getQuestion(AnswerModel answerModel){

        Call<ReceiveQuestionKnowledgeModel> receiveQuestionCall = service.receiveQuestionKnowledge(answerModel);

        receiveQuestionCall.enqueue(new Callback<ReceiveQuestionKnowledgeModel>() {
            @Override
            public void onResponse(Call<ReceiveQuestionKnowledgeModel> call, Response<ReceiveQuestionKnowledgeModel> response) {

                if(response.isSuccessful()){

                    if(response.body().toString() != null) {
                        Log.e("Response GK.", response.body().toString());
                    }
                    returnColor();
                    final GeneralKnowledgeModel generalKnowledgeModel = response.body().getQuestion();
                    int playerPoints = response.body().getPlayerPoints();
                    final int opponentPoints = response.body().getOpponentPoints();
                    final String opponentAnswer = response.body().getOpponentAnswer();



                    setEnabledButtons();
                    setAnswer(generalKnowledgeModel);
                    score.setText("My score: "+String.valueOf(playerPoints));
                    opponentScore.setText("Opponent\n score: "+String.valueOf(opponentPoints));
                    checkAnswer(generalKnowledgeModel);


                    //showOpponentsAnswer(opponentAnswer);



                }
                else{
                    if(response.message() != null) {
                        Log.e("Response GK.", response.message());
                    }
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReceiveQuestionKnowledgeModel> call, Throwable t) {
                if(t.getMessage() != null) {
                    Log.e("GK failure", t.getMessage());
                }

                Toast.makeText(getBaseContext(), "Sorry, but there is an error!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PlayKnowledgeActivity.this, MainMenuActivity.class));
                finish();
            }
        });

    }

    public boolean checkAnswer(GeneralKnowledgeModel generalKnowledgeModel){

        final String correctAnswer = generalKnowledgeModel.getCorrectAnswer();
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
