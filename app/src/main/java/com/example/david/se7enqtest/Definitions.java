package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.TokenModel;
import com.example.david.se7enqtest.models.WordDefinitionModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Definitions extends Activity {

    TextView questionRemain;
    TextView countdown;
    TextView question;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;
    WordDefinitionModel wordDefinitionModel;
    CountDownTimer timer;
    int counter = 20;
    ArrayList<WordDefinitionModel> arrayDefinition = new ArrayList<>(20);
    ArrayList<WordDefinitionModel> tempQuestionSet = new ArrayList<>(20);



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

        Call<List<WordDefinitionModel>>  wordDefinitionsCall = service.getWordDefinitions();

        wordDefinitionsCall.enqueue(new Callback<List<WordDefinitionModel>>() {
            @Override
            public void onResponse(Call<List<WordDefinitionModel>> call, Response<List<WordDefinitionModel>> response) {
                Log.i("Definicije Response", response.code()+" , "+response.message());

                if(response.code() == 200){

                    for(int i=0; i < 20; i++){
                        wordDefinitionModel = new WordDefinitionModel();

                        wordDefinitionModel.setId(response.body().get(i).getId());
                        wordDefinitionModel.setCorrectAnswer(response.body().get(i).getCorrectAnswer());
                        wordDefinitionModel.setWrongAnswer1(response.body().get(i).getWrongAnswer1());
                        wordDefinitionModel.setWrongAnswer2(response.body().get(i).getWrongAnswer2());
                        wordDefinitionModel.setWrongAnswer3(response.body().get(i).getWrongAnswer3());
                        wordDefinitionModel.setWord(response.body().get(i).getWord());

                        arrayDefinition.add(wordDefinitionModel);
                    }


                    for(WordDefinitionModel wd:arrayDefinition){
                        questionRemain.setText("Question remain: "+counter);
                        tempQuestionSet.add(wd);
                        setAnswer(wd);
                        startTimer();
                        break;
                    }






                }
                else if(response.code() == 400){
                    finish();
                    Toast.makeText(getBaseContext(), "You have been already trained in this category today\nCome back tomorrow", Toast.LENGTH_LONG).show();
                }
                else {
                    Log.e("Greska, Definicije!", response.message());
                }

            }

            @Override
            public void onFailure(Call<List<WordDefinitionModel>> call, Throwable t) {
                Log.e("DefinicijeFailure", t.getMessage());
                Toast.makeText(getBaseContext(), "Attempt failed, try again", Toast.LENGTH_LONG).show();
            }
        });




    }





    public void setAnswer(WordDefinitionModel wd){
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
                questionRemain.setText("Question remain: "+counter);
                final WordDefinitionModel wd = getQuestionSet();
                if(wd == null){
                    timer.cancel();

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Definitions.this);

                    alertDialog.setMessage("You have been trained in this category for today\nSee you tomorrow").setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Definitions.this, TrainingActivity.class));
                                    finish();

                                }
                            });

                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setAnswer(wd);
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
                                    onFinish();

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
                                    onFinish();


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
                                    onFinish();


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
                                    onFinish();


                                }
                            });





                        }
                    });
                    timer.start();
                }

            }
        }.start();

    }



    private WordDefinitionModel getQuestionSet(){
        WordDefinitionModel newQuestion = null;
        for (final WordDefinitionModel wd : arrayDefinition) {
            if(tempQuestionSet.contains(wd)){
            }
            else{
                tempQuestionSet.add(wd);
                return wd;
            }
        }

        return newQuestion;
    }

    public boolean checkAnswer(View v, WordDefinitionModel wd){
        Button b = (Button)v;


        String answer = b.getText().toString();
        if(wd.getCorrectAnswer().equals(answer)){
            b.setBackgroundColor(Color.GREEN);
            b.setTextColor(Color.BLACK);
            return true;
        }
        else{
            b.setBackgroundColor(Color.RED);
            b.setTextColor(Color.BLACK);

            if(answer1.getText().toString().equals(wd.getCorrectAnswer())){
                answer1.setBackgroundColor(Color.GREEN);
                answer1.setTextColor(Color.BLACK);
            }
            else if(answer2.getText().toString().equals(wd.getCorrectAnswer())){
                answer2.setBackgroundColor(Color.GREEN);
                answer2.setTextColor(Color.BLACK);
            }
            else if(answer3.getText().toString().equals(wd.getCorrectAnswer())){
                answer3.setBackgroundColor(Color.GREEN);
                answer3.setTextColor(Color.BLACK);
            }
            else if(answer4.getText().toString().equals(wd.getCorrectAnswer())){
                answer4.setBackgroundColor(Color.GREEN);
                answer4.setTextColor(Color.BLACK);
            }
            return false;
        }


    }


}
