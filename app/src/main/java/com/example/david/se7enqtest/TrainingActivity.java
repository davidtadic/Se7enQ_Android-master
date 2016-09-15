package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TrainingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Button array = (Button)findViewById(R.id.arrayButton);
        Button definitions = (Button)findViewById(R.id.definitionsButton);

        definitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainingActivity.this, Definitions.class);
                startActivity(intent);
            }
        });

    }
}
