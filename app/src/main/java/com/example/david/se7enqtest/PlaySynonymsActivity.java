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
import com.example.david.se7enqtest.models.ReceiveQuestionModel;
import com.example.david.se7enqtest.models.SynonymsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySynonymsActivity extends Activity implements View.OnClickListener{

    TextView score;
    TextView countdown;
    TextView opponentScore;
    TextView description;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    static int opponentPoints;
    static int points = 0;
    static int currentQuestionIndex = 0;
    CountDownTimer timer;
    ApiCall service;
    SharedPreferences settingsOpponent;
    String userAnswer;
    AnswerModel answerModel1;
    boolean correct = false;

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

        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);

        answerModel1 = new AnswerModel();
        answerModel1.setAnswer("first");
        answerModel1.setCorrect(false);
        answerModel1.setCurrentQuestonIndex(0);


        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        final String userToken = settings.getString("TOKEN","");

        //making a service
        service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);



        getQuestion(answerModel1);
        startQuestion();





        //save in opponent score and question index
        settingsOpponent = getSharedPreferences("PREF_GAME", 0);
        SharedPreferences.Editor editor = settingsOpponent.edit();
        editor.putInt("OPPONENT_SCORE", opponentPoints);
        editor.putInt("QUESTION_INDEX", currentQuestionIndex);
        editor.commit();

        //save in my score
        SharedPreferences settingsUser = getSharedPreferences("MY SCORE_PREF",0);
        SharedPreferences.Editor editorUser = settingsUser.edit();
        editorUser.putInt("MY_SCORE", points);
        editorUser.commit();

    }
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.answer1 && v.getId() == R.id.answer2){
            userAnswer = answer1.getText().toString()+" "+answer2.getText().toString();
        }
        if(v.getId() == R.id.answer1 && v.getId() == R.id.answer3){
            userAnswer = answer1.getText().toString()+" "+answer3.getText().toString();
        }
        if(v.getId() == R.id.answer1 && v.getId() == R.id.answer4){
            userAnswer = answer1.getText().toString()+" "+answer4.getText().toString();
        }
        if(v.getId() == R.id.answer2 && v.getId() == R.id.answer3){
            userAnswer = answer2.getText().toString()+" "+answer3.getText().toString();
        }
        if(v.getId() == R.id.answer2 && v.getId() == R.id.answer4){
            userAnswer = answer2.getText().toString()+" "+answer4.getText().toString();
        }
        if(v.getId() == R.id.answer3 && v.getId() == R.id.answer4){
            userAnswer = answer3.getText().toString()+" "+answer4.getText().toString();
        }

    }

    private AnswerModel getAnswerModel(){
        AnswerModel answerModel = new AnswerModel();
        answerModel.setAnswer(userAnswer);
        answerModel.setCorrect(correct);
        answerModel.setCurrentQuestonIndex(currentQuestionIndex);

        return answerModel;
    }

    private  void startQuestion(){
        timer = new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                final AnswerModel answerModel = getAnswerModel();
                currentQuestionIndex++;
                if(currentQuestionIndex == 4){
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



        Call<ReceiveQuestionModel> receiveQuestionCall = service.receiveQuestion(answerModel);

        receiveQuestionCall.enqueue(new Callback<ReceiveQuestionModel>() {
            @Override
            public void onResponse(Call<ReceiveQuestionModel> call, Response<ReceiveQuestionModel> response) {



                if(response.isSuccessful()){
                    ReceiveQuestionModel receiveQuestionModel = response.body();

                    SynonymsModel synonymsModel = (SynonymsModel)receiveQuestionModel.getTypeOfQuestion();
                    SynonymsModel synonymQuestion = new SynonymsModel();

                    synonymQuestion.setId(synonymsModel.getId());
                    synonymQuestion.setCorrectAnswer1(synonymsModel.getCorrectAnswer1());
                    synonymQuestion.setCorrectAnswer2(synonymsModel.getCorrectAnswer2());
                    synonymQuestion.setWrongAnswer1(synonymsModel.getWrongAnswer1());
                    synonymQuestion.setWrongAnswer2(synonymsModel.getWrongAnswer2());

                    String opponentAnswer = receiveQuestionModel.getOpponentAnswer();
                    String opponentAnswer1 = opponentAnswer.split(" ")[0];
                    String opponentAnswer2 = opponentAnswer.split(" ")[0];

                    opponentPoints = receiveQuestionModel.getOpponentPoints();



                    //set question on layout
                    setAnswer(synonymQuestion);

                    //checking a question
                    checkAnswer(synonymQuestion);
                    if(checkAnswer(synonymQuestion)){
                        correct = true;
                        points += 1;
                        score.setText(points);
                    }
                    else{
                        correct = false;
                        points -= 1;
                        score.setText(points);

                    }

                    //show opponent's answer
                    if(answer1.getText().toString().equals(opponentAnswer1) || answer1.getText().toString().equals(opponentAnswer2)){
                        answer1.setBackgroundColor(Color.YELLOW);
                        answer1.setTextColor(Color.BLACK);
                    }
                    else if(answer2.getText().toString().equals(opponentAnswer1) || answer2.getText().toString().equals(opponentAnswer2)){
                        answer2.setBackgroundColor(Color.YELLOW);
                        answer2.setTextColor(Color.BLACK);
                    }
                    else if(answer3.getText().toString().equals(opponentAnswer1) || answer3.getText().toString().equals(opponentAnswer2)){
                        answer2.setBackgroundColor(Color.YELLOW);
                        answer2.setTextColor(Color.BLACK);
                    }
                    else if(answer4.getText().toString().equals(opponentAnswer1) || answer4.getText().toString().equals(opponentAnswer2)){
                        answer2.setBackgroundColor(Color.YELLOW);
                        answer2.setTextColor(Color.BLACK);
                    }


                    opponentScore.setText(opponentPoints);





                }
                else{
                    Log.e("Response synonym", response.message()+" "+response.code());
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReceiveQuestionModel> call, Throwable t) {
                Log.e("Sinonimi", t.getMessage());

                Toast.makeText(getBaseContext(), "Sorry, but there is an error!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PlaySynonymsActivity.this, MainMenuActivity.class));
                finish();
            }
        });

    }



    private boolean checkAnswer(SynonymsModel synonymQuestion){

        final String correct1 = synonymQuestion.getCorrectAnswer1();
        final String correct2 = synonymQuestion.getCorrectAnswer2();

        if(answer1.isActivated() && answer2.isActivated()){
            if((answer1.getText().toString().equals(correct1) && answer2.getText().toString().equals(correct2)) ||
                    (answer1.getText().toString().equals(correct2) && answer2.getText().toString().equals(correct1))){
                answer1.setBackgroundColor(Color.GREEN);
                answer1.setTextColor(Color.BLACK);
                answer2.setBackgroundColor(Color.GREEN);
                answer2.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer1.setBackgroundColor(Color.RED);
            answer1.setTextColor(Color.BLACK);
            answer2.setBackgroundColor(Color.RED);
            answer2.setTextColor(Color.BLACK);

            return false;
        }
        if(answer1.isActivated() && answer3.isActivated()){
            if((answer1.getText().toString().equals(correct1) && answer3.getText().toString().equals(correct2)) ||
                    (answer1.getText().toString().equals(correct2) && answer3.getText().toString().equals(correct1))){
                answer1.setBackgroundColor(Color.GREEN);
                answer1.setTextColor(Color.BLACK);
                answer3.setBackgroundColor(Color.GREEN);
                answer3.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer1.setBackgroundColor(Color.RED);
            answer1.setTextColor(Color.BLACK);
            answer3.setBackgroundColor(Color.RED);
            answer3.setTextColor(Color.BLACK);

            return false;
        }
        if(answer1.isActivated() && answer4.isActivated()){
            if((answer1.getText().toString().equals(correct1) && answer4.getText().toString().equals(correct2)) ||
                    (answer1.getText().toString().equals(correct2) && answer4.getText().toString().equals(correct1))){
                answer1.setBackgroundColor(Color.GREEN);
                answer1.setTextColor(Color.BLACK);
                answer4.setBackgroundColor(Color.GREEN);
                answer4.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer1.setBackgroundColor(Color.RED);
            answer1.setTextColor(Color.BLACK);
            answer4.setBackgroundColor(Color.RED);
            answer4.setTextColor(Color.BLACK);

            return false;
        }

        if(answer2.isActivated() && answer3.isActivated()){
            if((answer2.getText().toString().equals(correct1) && answer3.getText().toString().equals(correct2)) ||
                    (answer2.getText().toString().equals(correct2) && answer3.getText().toString().equals(correct1))){
                answer2.setBackgroundColor(Color.GREEN);
                answer2.setTextColor(Color.BLACK);
                answer3.setBackgroundColor(Color.GREEN);
                answer3.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer2.setBackgroundColor(Color.RED);
            answer2.setTextColor(Color.BLACK);
            answer3.setBackgroundColor(Color.RED);
            answer3.setTextColor(Color.BLACK);

            return false;
        }
        if(answer2.isActivated() && answer4.isActivated()){
            if((answer2.getText().toString().equals(correct1) && answer4.getText().toString().equals(correct2)) ||
                    (answer2.getText().toString().equals(correct2) && answer4.getText().toString().equals(correct1))){
                answer2.setBackgroundColor(Color.GREEN);
                answer2.setTextColor(Color.BLACK);
                answer4.setBackgroundColor(Color.GREEN);
                answer4.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer2.setBackgroundColor(Color.RED);
            answer2.setTextColor(Color.BLACK);
            answer4.setBackgroundColor(Color.RED);
            answer4.setTextColor(Color.BLACK);

            return false;
        }
        if(answer3.isActivated() && answer4.isActivated()){
            if((answer3.getText().toString().equals(correct1) && answer4.getText().toString().equals(correct2)) ||
                    (answer3.getText().toString().equals(correct2) && answer4.getText().toString().equals(correct1))){
                answer3.setBackgroundColor(Color.GREEN);
                answer3.setTextColor(Color.BLACK);
                answer4.setBackgroundColor(Color.GREEN);
                answer4.setTextColor(Color.BLACK);

                return true;
            }
        }
        else{
            answer3.setBackgroundColor(Color.RED);
            answer3.setTextColor(Color.BLACK);
            answer4.setBackgroundColor(Color.RED);
            answer4.setTextColor(Color.BLACK);

            return false;
        }
        new Handler().postDelayed(new Runnable() {

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

        return false;
    }




}
