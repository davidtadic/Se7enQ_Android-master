package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.AnswerModel;
import com.example.david.se7enqtest.models.GeneralKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveQuestionKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveScore;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayFinishActivity extends Activity {

    TextView gameFinished;
    ImageView logo;
    TextView resultOfGame;
    TextView finishScore;
    ApiCall service1;
    ApiCall service2;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_finish);

        gameFinished = (TextView)findViewById(R.id.gameFinished);
        logo = (ImageView)findViewById(R.id.logoSplashFinish);
        resultOfGame = (TextView)findViewById(R.id.resultOfGame);
        finishScore = (TextView)findViewById(R.id.scoreFinish);
        ok = (Button)findViewById(R.id.ok);

        //getting token out of shared preference
        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        final String userToken = settings.getString("TOKEN","");

        //getting answer model from knowledge
        SharedPreferences settingsAnswer = getSharedPreferences("ANSWER_MODEL",0);
        boolean correctFirst = settingsAnswer.getBoolean("ANSWER_CORRECT_KNOWLEDGE",false);
        String answerFirst = settingsAnswer.getString("ANSWER_KNOWLEDGE","pera");

        AnswerModel answerModel1 = new AnswerModel();
        answerModel1.setAnswer(answerFirst);
        answerModel1.setCorrect(correctFirst);
        answerModel1.setQuestionIndex(20);

        //making a service
        service1 = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);
        //making a service for finish
        service2 = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        //get result of game
        getQuestion(answerModel1);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGame();
            }
        });


    }

    public void getQuestion(AnswerModel answerModel){

        Call<ReceiveScore> receiveQuestionCall = service1.receiveScore(answerModel);

        receiveQuestionCall.enqueue(new Callback<ReceiveScore>() {
            @Override
            public void onResponse(Call<ReceiveScore> call, Response<ReceiveScore> response) {

                if(response.isSuccessful()){


                    int playerPoints = response.body().getPlayerPoints();
                    int opponentPoints = response.body().getOpponentPoints();

                    if(playerPoints > opponentPoints){
                        resultOfGame.setText("YOU WIN");
                    }
                    else if(playerPoints == opponentPoints){
                        resultOfGame.setText("TIE");
                    }
                    else if(playerPoints < opponentPoints){
                        resultOfGame.setText("YOU LOSE");
                    }

                    finishScore.setText(String.valueOf(playerPoints)+" : "+String.valueOf(opponentPoints));
                }
                else{
                    if(response.message() != null) {
                        Log.e("RESPONSE_ERROR", response.message());
                    }
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReceiveScore> call, Throwable t) {
                if(t.getMessage() != null) {
                    Log.e("FAILURE_ERROR", t.getMessage());
                }

                Toast.makeText(getBaseContext(), "Sorry, but there is an error!", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void deleteGame(){

        Call<Void> deleteCall = service2.deleteGame();
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    if(response.message() != null) {
                        Log.e("ClickResponse", response.message());
                    }
                    startActivity(new Intent(PlayFinishActivity.this, MainMenuActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if(t.getMessage() != null){
                    Log.e("ClickFailure", t.getMessage());
                }
                startActivity(new Intent(PlayFinishActivity.this, MainMenuActivity.class));
                finish();
            }
        });
    }
}
