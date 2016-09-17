package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TrainingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Button synonyms = (Button)findViewById(R.id.synonymsButton);
        Button definitions = (Button)findViewById(R.id.definitionsButton);
        Button array = (Button)findViewById(R.id.arrayButton);
        Button memory = (Button)findViewById(R.id.memoryButton);
        Button projection = (Button)findViewById(R.id.projectionsButton);
        Button calculation = (Button)findViewById(R.id.calculationsButton);
        Button generalKnowledge = (Button)findViewById(R.id.knowledgeButton);

        synonyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(TrainingActivity.this, SplashScreenTraining.class);
                intent.putExtra("splash", id);
                startActivity(intent);
            }
        });

        definitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(TrainingActivity.this, SplashScreenTraining.class);
                intent.putExtra("splash", id);
                startActivity(intent);
            }
        });

        array.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(TrainingActivity.this, SplashScreenTraining.class);
                intent.putExtra("splash", id);
                startActivity(intent);
            }
        });

        memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Sorry, this game is not ready yet", Toast.LENGTH_LONG).show();
            }
        });

        projection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Sorry, this game is not ready yet", Toast.LENGTH_LONG).show();
            }
        });

        calculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(TrainingActivity.this, SplashScreenTraining.class);
                intent.putExtra("splash", id);
                startActivity(intent);
            }
        });

        generalKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(TrainingActivity.this, SplashScreenTraining.class);
                intent.putExtra("splash", id);
                startActivity(intent);
            }
        });



    }
}
